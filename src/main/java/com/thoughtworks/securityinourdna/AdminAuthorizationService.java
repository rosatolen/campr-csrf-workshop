package com.thoughtworks.securityinourdna;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdminAuthorizationService {

    private UserRepo userRepo;

    @Autowired
    public AdminAuthorizationService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public boolean loginAsAdmin(final String username, final String password) {
        try {
            return userRepo.login(username, password).equals("admin");
        } catch (InvalidCredentials e){
            return false;
        }
    }
}
