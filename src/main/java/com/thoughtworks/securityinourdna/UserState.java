package com.thoughtworks.securityinourdna;

public class UserState {

    private boolean loggedIn;

    public UserState(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }
}
