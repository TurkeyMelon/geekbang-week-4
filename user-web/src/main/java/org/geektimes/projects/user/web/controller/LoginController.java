package org.geektimes.projects.user.web.controller;

import org.geektimes.projects.user.domain.User;
import org.geektimes.projects.user.service.UserService;
import org.geektimes.web.mvc.controller.PageController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * @author Geoffrey
 */
public class LoginController implements PageController {

    @Resource(name = "bean/UserService")
    private UserService userService;

    @Path("/login-form")
    @GET
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return "login-form.jsp";
    }

    @Path("/login")
    @POST
    public String signIn(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        User loginUser = userService.queryUserByNameAndPassword(name, password);
        if (loginUser != null) {
            System.out.println("Login user: " + loginUser);
            return "login-success.jsp";
        }
        return "login-fail.jsp";
    }
}
