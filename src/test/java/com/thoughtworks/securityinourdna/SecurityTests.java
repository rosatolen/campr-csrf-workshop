package com.thoughtworks.securityinourdna;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = VendorPortalApplication.class)
@WebIntegrationTest()
public class SecurityTests {
    private final int HTTP_FORBIDDEN = 403;
    private final int HTTP_OK = 200;

    @Test
    public void admin_should_delete_user_with_csrf_token() throws Exception {
        // Given
        Map<String, NewCookie> sessionCookies = fetchSessionCookies();

        String sessionCookieValue = sessionCookies.get("JSESSIONID").getValue();
        String csrfCookieValue = sessionCookies.get("csrf-token").getValue();

        // When
        Response deletionResponse = postToDeleteVendor(sessionCookieValue, Optional.of(csrfCookieValue));

        // Then
        int status = deletionResponse.getStatus();
        assertThat(status, is(HTTP_OK));
    }

    @Test
    public void admin_cannot_delete_user_without_csrf_token() throws Exception {
        // Given
        Map<String, NewCookie> sessionCookies = fetchSessionCookies();
        String sessionCookieValue = sessionCookies.get("JSESSIONID").getValue();

        // When
        Response deletionResponse = postToDeleteVendor(sessionCookieValue, Optional.empty());

        // Then
        int status = deletionResponse.getStatus();
        assertThat(status, is(HTTP_FORBIDDEN));
    }

    @Test
    public void admin_cannot_delete_user_with_incorrect_csrf_token() throws Exception {
        // Given
        Map<String, NewCookie> sessionCookies = fetchSessionCookies();
        String sessionCookieValue = sessionCookies.get("JSESSIONID").getValue();

        // When
        Response deletionResponse = postToDeleteVendor(sessionCookieValue, Optional.of("incorrect!"));

        // Then
        int status = deletionResponse.getStatus();
        assertThat(status, is(HTTP_FORBIDDEN));
    }


    private Map<String, NewCookie> fetchSessionCookies() throws URISyntaxException {
        Client client = ClientBuilder.newClient();
        URI loginUrl = new URI("http://localhost:8080/session");
        Form form = new Form();
        form.param("username", "admin");
        form.param("password", "admin");
        return client.target(loginUrl)
                .request()
                .post(Entity.form(form))
                .getCookies();
    }

    private Response postToDeleteVendor(String sessionCookieValue, Optional<String> csrfCookieValue) throws URISyntaxException {
        Client client = ClientBuilder.newClient();
        URI userDeletionUrl = new URI("http://localhost:8080/vendor/delete/Recycling");

        Form deletionFormParameters = new Form();
        if (csrfCookieValue.isPresent()) {
            deletionFormParameters.param("csrf-token", csrfCookieValue.get());
        }

        return client.target(userDeletionUrl)
                .request()
                .cookie("JSESSIONID", sessionCookieValue)
                .post(Entity.form(deletionFormParameters));
    }
}
