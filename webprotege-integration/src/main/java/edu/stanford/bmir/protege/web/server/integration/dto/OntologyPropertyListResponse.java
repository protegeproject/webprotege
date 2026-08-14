package edu.stanford.bmir.protege.web.server.integration.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Response containing zero or more OWL property frames.
 */
public class OntologyPropertyListResponse {

    @Nonnull
    private final List<OntologyPropertyData> items;

    @JsonCreator
    public OntologyPropertyListResponse(@JsonProperty("items") @Nonnull List<OntologyPropertyData> items) {
        this.items = Collections.unmodifiableList(Objects.requireNonNull(items, "items"));
    }

    @Nonnull
    @JsonProperty("items")
    public List<OntologyPropertyData> getItems() {
        return items;
    }

    @JsonProperty("count")
    public int getCount() {
        return items.size();
    }
}
