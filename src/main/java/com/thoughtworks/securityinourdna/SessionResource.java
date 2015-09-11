package com.thoughtworks.securityinourdna;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
public class SessionResource {

    private final UserRepo userRepo;

    @Autowired
    public SessionResource(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @RequestMapping(value = "/session", method = RequestMethod.POST)
    public String createSession(@RequestParam(value = "vendor") String vendor,
                                @RequestParam(value = "password") String password)
            throws Exception {
        if (userRepo.login(vendor, password)) {
            return "Welcome " + vendor + "!";
        } else {
            return "Sorry, please check your vendor and password combination.";
        }
    }

    @RequestMapping(value = "/vendor/delete/{name}", method = RequestMethod.POST)
    public void deleteVendor(@PathVariable String name) throws SQLException {
        userRepo.delete(name);
    }
}
