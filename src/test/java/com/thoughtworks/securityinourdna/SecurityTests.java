package com.thoughtworks.securityinourdna;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = VendorPortalApplication.class)
@WebIntegrationTest()
public class SecurityTests {
    @Test
    public void shouldHaveCSRFProtection() throws IOException, InterruptedException {
        // Given
        URL userDeletionUrl = new URL("http://localhost:8080/vendor/delete/admin");

        // When
        int status = makeRequest(userDeletionUrl, "POST").getResponseCode();

        // Then
        int httpStatusCodeForbidden = 403;
        assertThat(status, is(httpStatusCodeForbidden));
    }

    private HttpURLConnection makeRequest(URL url, String requestMethod) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod(requestMethod);
        return connection;
    }
}
