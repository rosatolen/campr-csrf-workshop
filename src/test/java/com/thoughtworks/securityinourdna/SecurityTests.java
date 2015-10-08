package com.thoughtworks.securityinourdna;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = VendorPortalApplication.class)
@WebIntegrationTest()
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SecurityTests {
    private final int HTTP_FORBIDDEN = 403;
    private final int HTTP_OK = 200;

    @Test
    public void admin_should_delete_user_with_csrf_token() throws Exception {
        // Given
        Client client = ClientBuilder.newClient();

        URI loginUrl = new URI("http://localhost:8080/session");
        Form form = new Form();
        form.param("username", "admin");
        form.param("password", "admin");
        Map<String, NewCookie> cookieJar = client.target(loginUrl)
                .request()
                .post(Entity.form(form))
                .getCookies();
        String csrfCookieValue = cookieJar.get("csrf-token").getValue();
        String sessionCookieValue = cookieJar.get("JSESSIONID").getValue();
        URI userDeletionUrl = new URI("http://localhost:8080/vendor/delete/Recycling");

        // When
        Form deletionFormParameters = new Form();
        deletionFormParameters.param("csrf-token", csrfCookieValue);

        Response deletionResponse = client.target(userDeletionUrl)
                .request()
                .cookie("JSESSIONID", sessionCookieValue)
                .cookie("csrf-token", csrfCookieValue)
                .post(Entity.form(deletionFormParameters));

        // Then
        int status = deletionResponse.getStatus();
        assertThat(status, is(HTTP_OK));
    }

    @Test
    public void sshouldHaveCSRFProtection() throws Exception {
        // Given
        Client client = ClientBuilder.newClient();

        URI loginUrl = new URI("http://localhost:8080/session");
        Form form = new Form();
        form.param("username", "admin");
        form.param("password", "admin");
        Map<String, NewCookie> cookieJar = client.target(loginUrl)
                .request()
                .post(Entity.form(form))
                .getCookies();
        String sessionCookieValue = cookieJar.get("JSESSIONID").getValue();
        URI userDeletionUrl = new URI("http://localhost:8080/vendor/delete/Electrician");

        // When
        Response deletionResponse = client.target(userDeletionUrl)
                .request()
                .cookie("JSESSIONID", sessionCookieValue)
                .post(Entity.form(new Form()));

        // Then
        int status = deletionResponse.getStatus();
        assertThat(status, is(HTTP_FORBIDDEN));
    }
}
