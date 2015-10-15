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
    public String deleteVendor(@PathVariable(value = "name")
                               String name,
                               HttpSession session,
                               // TODO: Do something with these... but first, features!
                               @RequestParam(value = "csrfToken", required = false)
                               String csrfToken,
                               HttpServletRequest request) throws SQLException {
        if (loggedIn(session) && csrfTokenCorrect(csrfToken, request.getCookies())) {
            userRepo.delete(name);
            return name;
        }

        throw new UnauthorizedResponse();
    }

    private boolean loggedIn(HttpSession session) {
        final UserState userState = (UserState) session.getAttribute("userState");
        return userState != null && userState.isLoggedIn();
    }

    private boolean csrfTokenCorrect(String csrfToken, Cookie[] cookies) {
        String csrfTokenFromCookie = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("csrfToken")) {
                csrfTokenFromCookie = cookie.getValue();
            }
        }

        if (csrfTokenFromCookie == null) {
            return false;
        } else {
            return constantTimeEquals(csrfTokenFromCookie, csrfToken);
        }
    }

    /*
     * http://codahale.com/a-lesson-in-timing-attacks/
     */
    private boolean constantTimeEquals(String a, String b) {
        byte[] byteArrayA = a.getBytes();
        byte[] byteArrayB = b.getBytes();
        
        if (byteArrayA.length != byteArrayB.length) {
            return false;
        }

        int result = 0;
        for (int i = 0; i < byteArrayA.length; i++) {
            result |= byteArrayA[i] ^ byteArrayB[i];
        }
        return result == 0;
    }

}
