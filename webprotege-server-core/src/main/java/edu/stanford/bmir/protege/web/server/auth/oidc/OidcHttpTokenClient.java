package edu.stanford.bmir.protege.web.server.auth.oidc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class OidcHttpTokenClient {

    private final ObjectMapper objectMapper;

    @Inject
    public OidcHttpTokenClient(@Nonnull ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Nonnull
    public OidcTokenResponse exchangeAuthorizationCode(@Nonnull String tokenEndpoint,
                                                       @Nonnull String code,
                                                       @Nonnull String redirectUri,
                                                       @Nonnull String clientId,
                                                       @Nonnull String clientSecret) throws IOException {
        HttpPost post = new HttpPost(tokenEndpoint);
        List<NameValuePair> form = new ArrayList<>();
        form.add(new BasicNameValuePair("grant_type", "authorization_code"));
        form.add(new BasicNameValuePair("code", code));
        form.add(new BasicNameValuePair("redirect_uri", redirectUri));
        form.add(new BasicNameValuePair("client_id", clientId));
        form.add(new BasicNameValuePair("client_secret", clientSecret));
        post.setEntity(new UrlEncodedFormEntity(form, StandardCharsets.UTF_8));
        try (CloseableHttpClient client = HttpClients.createSystem();
             CloseableHttpResponse response = client.execute(post)) {
            int status = response.getStatusLine().getStatusCode();
            String body = response.getEntity() == null ? "" : EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            if (status < 200 || status >= 300) {
                throw new IOException("Token endpoint returned HTTP " + status + ": " + body);
            }
            return OidcTokenResponse.parse(body, objectMapper);
        }
    }
}
