package edu.stanford.bmir.protege.web.server.auth.oidc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.annotation.Nonnull;
import java.io.IOException;

final class OidcTokenResponse {

    @Nonnull
    private final String idToken;

    private OidcTokenResponse(@Nonnull String idToken) {
        this.idToken = idToken;
    }

    @Nonnull
    String getIdToken() {
        return idToken;
    }

    @Nonnull
    static OidcTokenResponse parse(@Nonnull String json, @Nonnull ObjectMapper mapper) throws IOException {
        JsonNode root = mapper.readTree(json);
        JsonNode id = root.get("id_token");
        if (id == null || !id.isTextual() || id.asText().isEmpty()) {
            throw new IOException("Token response missing id_token");
        }
        return new OidcTokenResponse(id.asText());
    }
}
