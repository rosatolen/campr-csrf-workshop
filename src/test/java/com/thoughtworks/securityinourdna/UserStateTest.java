package com.thoughtworks.securityinourdna;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class UserStateTest {
    @Test
    public void shouldStoreLoggedInState() {
        final UserState userState = new UserState(true);

        assertThat(userState.isLoggedIn(), is(true));
    }
}
