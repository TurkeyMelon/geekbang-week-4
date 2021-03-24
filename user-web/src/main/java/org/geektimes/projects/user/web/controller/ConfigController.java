package org.geektimes.projects.user.web.controller;

import org.eclipse.microprofile.config.Config;
import org.geektimes.web.mvc.controller.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * @author Geoffrey
 */
public class ConfigController implements RestController {

    @Path("/config")
    @GET
    public String config(HttpServletRequest request, HttpServletResponse response) {
        Config config = (Config) request.getServletContext().getAttribute("config");
        return config.getValue("application.name", String.class);
    }
}
