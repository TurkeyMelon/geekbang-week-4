package org.geektimes.web.mvc.request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletRequestAttributes {

    private final HttpServletRequest request;

    private HttpServletResponse response;

    public ServletRequestAttributes(HttpServletRequest request) {
        this.request = request;
    }

    public ServletRequestAttributes(HttpServletRequest request, HttpServletResponse response) {
        this(request);
        this.response = response;
    }

    public final HttpServletRequest getRequest() {
        return this.request;
    }

    public final HttpServletResponse getResponse() {
        return this.response;
    }

}
