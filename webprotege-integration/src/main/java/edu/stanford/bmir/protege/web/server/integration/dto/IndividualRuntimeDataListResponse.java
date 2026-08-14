package edu.stanford.bmir.protege.web.server.integration.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Response containing zero or more individual runtime data records.
 */
public class IndividualRuntimeDataListResponse {

    @Nonnull
    private final List<IndividualRuntimeData> items;

    @JsonCreator
    public IndividualRuntimeDataListResponse(@JsonProperty("items") @Nonnull List<IndividualRuntimeData> items) {
        this.items = Collections.unmodifiableList(Objects.requireNonNull(items, "items"));
    }

    @Nonnull
    @JsonProperty("items")
    public List<IndividualRuntimeData> getItems() {
        return items;
    }

    @JsonProperty("count")
    public int getCount() {
        return items.size();
    }
}
