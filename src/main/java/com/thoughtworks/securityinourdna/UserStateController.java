package com.thoughtworks.securityinourdna;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
public class UserStateController {

    private final AdminAuthorizationService adminAuthorizationService;

    @Autowired
    public UserStateController(AdminAuthorizationService adminAuthorizationService) {
        this.adminAuthorizationService = adminAuthorizationService;
    }

    @RequestMapping(value = "/session", method = RequestMethod.POST)
    public String createSession(@RequestParam(value = "username") String username,
                                @RequestParam(value = "password") String password,
                                HttpSession session) {

        final boolean loggedIn = adminAuthorizationService.loginAsAdmin(username, password);

        final UserState userState = new UserState(loggedIn);
        session.setAttribute("userState", userState);

        if (loggedIn) {
            return "admin.html";
        } else {
            return "Sorry! There is something wrong with your username and password combination.";
        }
    }

    @RequestMapping(value = "/service/logout", method = RequestMethod.POST)
    public void logout (final HttpSession session) {
        UserState userState = new UserState(false);
        session.setAttribute("userState", userState);
    }
}
