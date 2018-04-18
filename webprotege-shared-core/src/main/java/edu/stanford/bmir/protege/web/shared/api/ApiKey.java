package edu.stanford.bmir.protege.web.shared.api;

import com.fasterxml.jackson.annotation.JsonValue;
import com.google.gwt.core.shared.GwtIncompatible;

import javax.annotation.Nonnull;

import java.util.UUID;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 15 Apr 2018
 */
public class ApiKey {

    private final String key;

    private ApiKey(String key) {
        this.key = key;
    }

    public static ApiKey valueOf(@Nonnull String key) {
        return new ApiKey(checkNotNull(key));
    }

    @GwtIncompatible
    @Nonnull
    public static ApiKey generateApiKey() {
        String uuid = UUID.randomUUID().toString();
        return new ApiKey(uuid);
    }

    @JsonValue
    @Nonnull
    public String getKey() {
        return key;
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ApiKey)) {
            return false;
        }
        ApiKey other = (ApiKey) obj;
        return this.key.equals(other.key);
    }


    @Override
    public String toString() {
        return toStringHelper("ApiKey")
                .addValue(key)
                .toString();
    }
}
