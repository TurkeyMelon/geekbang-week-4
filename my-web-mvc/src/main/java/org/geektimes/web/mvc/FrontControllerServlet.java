package org.geektimes.web.mvc;

import org.apache.commons.lang.StringUtils;
import org.geektimes.web.mvc.controller.Controller;
import org.geektimes.web.mvc.controller.PageController;
import org.geektimes.web.mvc.controller.RestController;
import org.geektimes.web.mvc.exception.BindException;
import org.geektimes.web.mvc.request.RequestContextHolder;
import org.geektimes.web.mvc.request.ServletRequestAttributes;
import org.geektimes.web.mvc.util.JsonUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Path;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.apache.commons.lang.StringUtils.substringAfter;

public class FrontControllerServlet extends HttpServlet {

    /**
     * 请求路径和 Controller 的映射关系缓存
     */
//    private final Map<String, Controller> controllersMapping = new HashMap<>();

    /**
     * 请求路径和 {@link HandlerMethodInfo} 映射关系缓存
     */
    private final Map<String, HandlerMethodInfo> handleMethodInfoMapping = new HashMap<>();

    public static final String COMPONENTS_FACTORY = "componentsFactory";

    private final Map<Class<?>, Object> componentsMap = new LinkedHashMap<>();

    /**
     * 初始化 Servlet
     *
     * @param servletConfig
     */
    @Override
    public void init(ServletConfig servletConfig) {
        initComponentMap(servletConfig.getServletContext());
        initHandleMethods();
    }

    @SuppressWarnings("unchecked")
    private void initComponentMap(ServletContext servletContext) {
        System.out.println("Initializing components map for " + FrontControllerServlet.class.getName());
        Map<Class<?>, Object> initializedComponentsMap = (Map<Class<?>, Object>) servletContext.getAttribute(COMPONENTS_FACTORY);
        if (initializedComponentsMap != null) {
            this.componentsMap.putAll(initializedComponentsMap);
        }
    }

    /**
     * 读取所有的 RestController 的注解元信息 @Path
     * 利用 ServiceLoader 技术（Java SPI）
     */
    private void initHandleMethods() {
        for (Controller controller : ServiceLoader.load(Controller.class)) {
            Class<?> controllerClass = controller.getClass();
            Path pathFromClass = controllerClass.getAnnotation(Path.class);
            String requestPath = pathFromClass != null ? pathFromClass.value() : null;
            Method[] publicMethods = controllerClass.getDeclaredMethods();
            // 处理方法支持的 HTTP 方法集合
            Stream.of(publicMethods)
                    .filter(method -> !Modifier.isStatic(method.getModifiers()) &&
                            Modifier.isPublic(method.getModifiers()))
                    .forEach(method -> {
                        Set<String> supportedHttpMethods = findSupportedHttpMethods(method);
                        Path pathFromMethod = method.getAnnotation(Path.class);
                        String pathFromMethodStr = pathFromMethod != null ? pathFromMethod.value() : "";
                        Controller initializedController = (Controller) getObject(controllerClass);
                        handleMethodInfoMapping.put(requestPath != null ?
                                        requestPath + addSlashIfAbsent(pathFromMethodStr) : addSlashIfAbsent(pathFromMethodStr),
                                new HandlerMethodInfo(requestPath != null ?
                                        requestPath + addSlashIfAbsent(pathFromMethodStr) : addSlashIfAbsent(pathFromMethodStr),
                                        method, supportedHttpMethods,
                                        initializedController != null ? initializedController : controller));
                    });
//            controllersMapping.put(requestPath, controller);
        }
    }

    private Object getObject(Class<?> clazz) {
        if (!componentsMap.containsKey(clazz)) {
            return null;
        }
        return componentsMap.get(clazz);
    }

    /**
     * 获取处理方法中标注的 HTTP方法集合
     *
     * @param method 处理方法
     * @return
     */
    private Set<String> findSupportedHttpMethods(Method method) {
        Set<String> supportedHttpMethods = new LinkedHashSet<>();
        for (Annotation annotationFromMethod : method.getAnnotations()) {
            HttpMethod httpMethod = annotationFromMethod.annotationType().getAnnotation(HttpMethod.class);
            if (httpMethod != null) {
                supportedHttpMethods.add(httpMethod.value());
            }
        }

        if (supportedHttpMethods.isEmpty()) {
            supportedHttpMethods.addAll(asList(HttpMethod.GET, HttpMethod.POST,
                    HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.HEAD, HttpMethod.OPTIONS));
        }

        return supportedHttpMethods;
    }

    /**
     * SCWCD
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 建立映射关系
        // requestURI = /a/hello/world
        String requestURI = request.getRequestURI();
        // contextPath  = /a or "/" or ""
        String servletContextPath = request.getContextPath();
        // 映射路径（子路径）
        String requestMappingPath = substringAfter(requestURI,
                StringUtils.replace(servletContextPath, "//", "/"));
        // 映射到 Controller
//        Controller controller = controllersMapping.get(requestMappingPath);
        HandlerMethodInfo handlerMethodInfo = handleMethodInfoMapping.get(requestMappingPath);
        if (handlerMethodInfo == null) {
            return;
        }
        Controller controller = handlerMethodInfo.getController();

        if (controller == null) {
            return;
        }

        if (!handlerMethodInfo.getSupportedHttpMethods().contains(request.getMethod())) {
            // HTTP 方法不支持
            response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }

        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request, response));
        try {
            if (controller instanceof PageController) {
//                PageController pageController = PageController.class.cast(controller);
                String viewPath = String.valueOf(handlerMethodInfo.getHandlerMethod().invoke(controller, request, response));
                // 页面请求 forward
                // request -> RequestDispatcher forward
                // RequestDispatcher requestDispatcher = request.getRequestDispatcher(viewPath);
                // ServletContext -> RequestDispatcher forward
                // ServletContext -> RequestDispatcher 必须以 "/" 开头
                ServletContext servletContext = request.getServletContext();
                viewPath = addSlashIfAbsent(viewPath);
                RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(viewPath);
                requestDispatcher.forward(request, response);
            } else if (controller instanceof RestController) {
                Method handlerMethod = handlerMethodInfo.getHandlerMethod();
                int parameterCount = handlerMethod.getParameterCount();
                // TODO 硬编码可修改
                Object result;
                if (parameterCount == 2) {
                    result = handlerMethod.invoke(controller, request, response);
                } else {
                    result = handlerMethod.invoke(controller);
                }
                writeToResponse(response, HttpServletResponse.SC_OK, result);
            }
        } catch (Throwable throwable) {
            if (throwable.getCause() instanceof IOException) {
                throw (IOException) throwable.getCause();
            } else if (throwable.getCause() instanceof BindException) {
                System.err.println(throwable.getCause().getMessage());
                writeToResponse(response, HttpServletResponse.SC_BAD_REQUEST, ((BindException) throwable.getCause()).getErrorMap());
            } else {
                throw new ServletException(throwable.getCause());
            }
        } finally {
            RequestContextHolder.resetRequestAttributes();
        }
    }

    private void writeToResponse(HttpServletResponse response, int httpStatus, Object message) throws IOException {
        response.setStatus(httpStatus);
        response.setContentType("application/json");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        PrintWriter writer = response.getWriter();
        String jsonStr = JsonUtil.objectToJson(message);
        System.out.println("Result Json: " + jsonStr);
        writer.print(jsonStr);
        writer.flush();
    }

    private String addSlashIfAbsent(String path) {
        if (path == null || "".equals(path)) {
            return "";
        }
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        return path;
    }

//    private void beforeInvoke(Method handleMethod, HttpServletRequest request, HttpServletResponse response) {
//
//        CacheControl cacheControl = handleMethod.getAnnotation(CacheControl.class);
//
//        Map<String, List<String>> headers = new LinkedHashMap<>();
//
//        if (cacheControl != null) {
//            CacheControlHeaderWriter writer = new CacheControlHeaderWriter();
//            writer.write(headers, cacheControl.value());
//        }
//    }
}
