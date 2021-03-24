package org.geektimes.web.mvc.request;

/**
 * @see "org.springframework.web.context.request.RequestContextHolder"
 */
public abstract class RequestContextHolder {

    private static final ThreadLocal<ServletRequestAttributes> requestAttributesHolder =
            new ThreadLocal<>();

    private static final ThreadLocal<ServletRequestAttributes> inheritableRequestAttributesHolder =
            new InheritableThreadLocal<>();

    public static void resetRequestAttributes() {
        requestAttributesHolder.remove();
        inheritableRequestAttributesHolder.remove();
    }

    public static void setRequestAttributes(ServletRequestAttributes attributes) {
        setRequestAttributes(attributes, false);
    }

    public static void setRequestAttributes(ServletRequestAttributes attributes, boolean inheritable) {
        if (attributes == null) {
            resetRequestAttributes();
        } else {
            if (inheritable) {
                inheritableRequestAttributesHolder.set(attributes);
                requestAttributesHolder.remove();
            } else {
                requestAttributesHolder.set(attributes);
                inheritableRequestAttributesHolder.remove();
            }
        }
    }

    public static ServletRequestAttributes getRequestAttributes() {
        ServletRequestAttributes attributes = requestAttributesHolder.get();
        if (attributes == null) {
            attributes = inheritableRequestAttributesHolder.get();
        }
        return attributes;
    }

}
