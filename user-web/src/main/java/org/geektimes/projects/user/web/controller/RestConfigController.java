package org.geektimes.projects.user.web.controller;

import org.eclipse.microprofile.config.Config;
import org.geektimes.configuration.microprofile.config.servlet.ConfigServletRequestListener;
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
        // 使用ThreadLocal取得 Config
        Config config = ConfigServletRequestListener.getConfig();
        return config.getValue("application.name", String.class);
    }
}
