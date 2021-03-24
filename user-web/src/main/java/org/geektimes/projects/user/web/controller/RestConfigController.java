package org.geektimes.projects.user.web.controller;

import org.eclipse.microprofile.config.Config;
import org.geektimes.web.mvc.controller.RestController;
import org.geektimes.web.mvc.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * @author Geoffrey
 */
public class RestConfigController implements RestController {

    @Path("/rest-config")
    @GET
    public String config() {
        // 使用ThreadLocal取得 HttpServletRequest
        // TODO 可扩展 RequestContextHolder 中的 Attributes 为key/value存储多样的属性值
        HttpServletRequest request = RequestContextHolder.getRequestAttributes().getRequest();
        Config config = (Config) request.getServletContext().getAttribute("config");
        return config.getValue("application.name", String.class);
    }
}
