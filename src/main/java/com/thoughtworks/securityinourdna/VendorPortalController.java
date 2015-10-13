package com.thoughtworks.securityinourdna;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;

@RestController
public class VendorPortalController {

    private final UserRepo userRepo;

    @Autowired
    public VendorPortalController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @RequestMapping(value = "/vendor/delete/{name}", method = RequestMethod.POST)
    public String deleteVendor(@PathVariable String name,
                               @RequestParam String csrfToken,
                               HttpServletRequest request,
                               HttpSession session) throws SQLException {
        if (loggedIn(session) && csrfTokenCorrect(csrfToken, request)) {
            userRepo.delete(name);
            return name;
        }

        throw new UnauthorizedResponse();
    }

    private boolean loggedIn(HttpSession session) {
        final UserState userState = (UserState) session.getAttribute("userState");
        return userState != null && userState.isLoggedIn();
    }

    private boolean csrfTokenCorrect(String csrfToken, HttpServletRequest request) {
        String csrfTokenFromCookie = null;
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("csrfToken")) {
                csrfTokenFromCookie = cookie.getValue();
            }
        }

        if (csrfTokenFromCookie == null) {
            return false;
        } else {
            return csrfTokenFromCookie.equals(csrfToken);
        }
    }

}
