package org.geektimes.projects.user.web.controller;

import org.geektimes.projects.user.domain.User;
import org.geektimes.projects.user.service.UserService;
import org.geektimes.web.mvc.controller.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.Collection;

/**
 * @author Geoffrey
 */
@Path("/user")
public class UserController implements RestController {

    @Resource(name = "bean/UserService")
    private UserService userService;

    @GET
    public Collection<User> getAll(HttpServletRequest request, HttpServletResponse response) {
        return userService.getAll();
    }

}
