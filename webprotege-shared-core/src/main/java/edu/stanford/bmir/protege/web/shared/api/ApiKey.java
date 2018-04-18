package edu.stanford.bmir.protege.web.shared.api;

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

    private final String id;

    private ApiKey(String id) {
        this.id = id;
    }

    public static ApiKey valueOf(@Nonnull String id) {
        return new ApiKey(checkNotNull(id));
    }

    @GwtIncompatible
    @Nonnull
    public static ApiKey generateApiKey() {
        String uuid = UUID.randomUUID().toString();
        return new ApiKey(uuid);
    }

    public String getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
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
        return this.id.equals(other.id);
    }


    @Override
    public String toString() {
        return toStringHelper("ApiKey")
                .addValue(id)
                .toString();
    }
}
