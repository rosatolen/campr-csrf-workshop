package com.thoughtworks.securityinourdna;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
                               @CookieValue(value = "csrfToken", required = false)
                               String csrfTokenFromCookie) throws SQLException {
        if (loggedIn(session)) {
            userRepo.delete(name);
            return name;
        }

        throw new UnauthorizedResponse();
    }

    private boolean loggedIn(HttpSession session) {
        final UserState userState = (UserState) session.getAttribute("userState");
        return userState != null && userState.isLoggedIn();
    }
}
