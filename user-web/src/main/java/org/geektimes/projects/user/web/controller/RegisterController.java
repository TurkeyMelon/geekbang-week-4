package org.geektimes.projects.user.web.controller;

import org.apache.commons.lang.StringUtils;
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
public class RegisterController implements PageController {

    @Resource(name = "bean/UserService")
    private UserService userService;

    @Path("/register")
    @POST
    public String register(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phoneNumber");
        if (StringUtils.isBlank(name) || StringUtils.isBlank(password) || StringUtils.isBlank(email) || StringUtils.isBlank(phoneNumber)) {
            return "fail.jsp";
        }
        User newUser = new User();
        newUser.setName(name);
        newUser.setPassword(password);
        newUser.setEmail(email);
        newUser.setPhoneNumber(phoneNumber);
        if (userService.register(newUser)) {
            return "login-form.jsp";
        }
        return "fail.jsp";
    }

    @Path("/register-form")
    @GET
    public String registerPage(HttpServletRequest request, HttpServletResponse response) {
        return "register-form.jsp";
    }
}
