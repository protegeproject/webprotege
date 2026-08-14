package edu.stanford.bmir.protege.web.server.integration.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Request body for creating or replacing individual runtime data via PUT.
 */
public class IndividualRuntimeDataRequest {

    @Nullable
    private final String individualIri;

    @Nonnull
    private final Map<String, String> properties;

    @JsonCreator
    public IndividualRuntimeDataRequest(@JsonProperty("individualIri") @Nullable String individualIri,
                                        @JsonProperty("properties") @Nullable Map<String, String> properties) {
        this.individualIri = individualIri;
        this.properties = properties == null
                ? Collections.emptyMap()
                : Collections.unmodifiableMap(new LinkedHashMap<>(properties));
    }

    @Nullable
    @JsonProperty("individualIri")
    public String getIndividualIri() {
        return individualIri;
    }

    @Nonnull
    @JsonProperty("properties")
    public Map<String, String> getProperties() {
        return properties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IndividualRuntimeDataRequest)) {
            return false;
        }
        IndividualRuntimeDataRequest that = (IndividualRuntimeDataRequest) o;
        return Objects.equals(individualIri, that.individualIri) && properties.equals(that.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(individualIri, properties);
    }
}
