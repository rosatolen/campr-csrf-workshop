package com.thoughtworks.securityinourdna;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = VendorPortal.class)
@WebIntegrationTest()
public class CSRFAppSecTest {
    @Test
    public void unauthorizedUsers_shouldNotBeAbleToDeleteAVendor() throws IOException, InterruptedException {
        // Given
        URL userDeletionUrl = new URL("http://localhost:8080/vendor/delete/admin");

        // When
        int status = makePostRequest(userDeletionUrl);

        // Then
        int httpStatusCodeForbidden = 403;
        assertThat(status, is(httpStatusCodeForbidden));
    }

    private int makePostRequest(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        return connection.getResponseCode();
    }
}
