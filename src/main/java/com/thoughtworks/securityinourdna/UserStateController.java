package com.thoughtworks.securityinourdna;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.SecureRandom;

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
                                HttpSession session, HttpServletResponse response) {

        final boolean loggedIn = adminAuthorizationService.loginAsAdmin(username, password);

        final UserState userState = new UserState(loggedIn);
        session.setAttribute("userState", userState);

        if (loggedIn) {
            response.addCookie(new Cookie("csrf-token", generateCSRFToken()));
            return "admin.html";
        } else {
            return "Sorry! There is something wrong with your username and password combination.";
        }
    }

    private String generateCSRFToken() {
        SecureRandom entropy = new SecureRandom();

        byte secureBytes[] = new byte[20];
        entropy.nextBytes(secureBytes);

        return Base64.encodeBase64String(secureBytes);
    }

    @RequestMapping(value = "/service/logout", method = RequestMethod.POST)
    public void logout (final HttpSession session) {
        UserState userState = new UserState(false);
        session.setAttribute("userState", userState);
    }
}
