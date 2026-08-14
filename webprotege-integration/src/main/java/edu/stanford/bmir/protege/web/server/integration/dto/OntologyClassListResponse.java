package edu.stanford.bmir.protege.web.server.integration.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Response containing zero or more OWL class frames.
 */
public class OntologyClassListResponse {

    @Nonnull
    private final List<OntologyClassData> items;

    @JsonCreator
    public OntologyClassListResponse(@JsonProperty("items") @Nonnull List<OntologyClassData> items) {
        this.items = Collections.unmodifiableList(Objects.requireNonNull(items, "items"));
    }

    @Nonnull
    @JsonProperty("items")
    public List<OntologyClassData> getItems() {
        return items;
    }

    @JsonProperty("count")
    public int getCount() {
        return items.size();
    }
}
