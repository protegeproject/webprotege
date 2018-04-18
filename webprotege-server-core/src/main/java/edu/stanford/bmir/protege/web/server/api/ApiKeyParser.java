package edu.stanford.bmir.protege.web.server.api;

import edu.stanford.bmir.protege.web.shared.api.ApiKey;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Apr 2018
 */
public class ApiKeyParser {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    private static final Pattern PATTERN = Pattern.compile("apikey\\s+(.+)", Pattern.CASE_INSENSITIVE);

    /**
     * Parses the API key from the specified header value.  The header value is expected to be the string
     * "apikey" followed by white space, followed by the api key string.  For example,
     * "apikey my-api-key-here"
     * @param headerValue The header value.  This may be null.  If it is null then an Optional.empty() value
     *                    will be returned.
     * @return The API key.  If the key does not match the expected pattern then Optional.empty() is returned.
     */
    public static Optional<ApiKey> parseApiKey(@Nullable String headerValue) {
        if(headerValue == null) {
            return Optional.empty();
        }
        Matcher matcher = PATTERN.matcher(headerValue.trim());
        if(matcher.matches()) {
            String key = matcher.group(1);
            return Optional.of(ApiKey.valueOf(key));
        }
        else {
            return Optional.empty();
        }
    }
}
