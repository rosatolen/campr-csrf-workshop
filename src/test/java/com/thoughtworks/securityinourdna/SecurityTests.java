package com.thoughtworks.securityinourdna;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = VendorPortalApplication.class)
@WebIntegrationTest()
public class SecurityTests {
    private final int HTTP_UNAUTHORIZED = 401;
    private final int HTTP_BAD_REQUEST = 400;
    private final int HTTP_OK = 200;

    @Test
    public void admin_should_delete_user_with_csrf_token() throws Exception {
        // Given
        final BasicCookieStore cookieStore = new BasicCookieStore();
        loginAsAdmin(cookieStore);

        String csrfToken = null;
        for (Cookie c : cookieStore.getCookies()) {
            if (c.getName().equals("csrfToken")) {
                csrfToken = c.getValue();
                break;
            }
        }

        // When
        int status = postToDeleteVendor(cookieStore, csrfToken);

        // Then
        assertThat(status, is(HTTP_OK));
    }

    @Test
    public void admin_cannot_delete_user_with_incorrect_csrf_token() throws Exception {
        // Given
        BasicCookieStore cookieStore = new BasicCookieStore();
        loginAsAdmin(cookieStore);

        // When
        int status = postToDeleteVendor(cookieStore, "incorrect!");

        // Then
        assertThat(status, is(HTTP_UNAUTHORIZED));
    }


    private void loginAsAdmin(BasicCookieStore cookieStore) throws Exception {
        CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultCookieStore(cookieStore)
                .build();

        RequestBuilder request = RequestBuilder.post("http://localhost:8080/session")
                .addParameter("username", "admin")
                .addParameter("password", "admin");

        httpclient.execute(request.build());
    }

    private int postToDeleteVendor(BasicCookieStore cookieStore, String maybeCsrfCookieValue) throws Exception {
        CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultCookieStore(cookieStore)
                .build();

        RequestBuilder request = RequestBuilder.post("http://localhost:8080/vendor/delete/Recycling");

        if (maybeCsrfCookieValue != null) {
            request.addParameter("csrfToken", maybeCsrfCookieValue);
        }

        final CloseableHttpResponse response = httpclient.execute(request.build());

        return response.getStatusLine().getStatusCode();
    }
}
