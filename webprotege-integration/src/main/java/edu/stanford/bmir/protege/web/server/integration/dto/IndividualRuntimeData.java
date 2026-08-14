package edu.stanford.bmir.protege.web.server.integration.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Runtime data associated with a named individual in a project.
 * Intended for third-party system integration (sensor values, device state, etc.).
 */
public class IndividualRuntimeData {

    @Nonnull
    private final String projectId;

    @Nonnull
    private final String individualIri;

    @Nonnull
    private final Map<String, String> properties;

    @Nonnull
    private final List<String> types;

    private final long updatedAt;

    @Nullable
    private final String updatedBy;

    @JsonCreator
    public IndividualRuntimeData(@JsonProperty("projectId") @Nonnull String projectId,
                                 @JsonProperty("individualIri") @Nonnull String individualIri,
                                 @JsonProperty("properties") @Nullable Map<String, String> properties,
                                 @JsonProperty("updatedAt") long updatedAt,
                                 @JsonProperty("updatedBy") @Nullable String updatedBy,
                                 @JsonProperty("types") @Nullable List<String> types) {
        this.projectId = Objects.requireNonNull(projectId, "projectId");
        this.individualIri = Objects.requireNonNull(individualIri, "individualIri");
        this.properties = properties == null
                ? Collections.emptyMap()
                : Collections.unmodifiableMap(new LinkedHashMap<>(properties));
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
        this.types = copyIris(types);
    }

    public IndividualRuntimeData(@Nonnull String projectId,
                                 @Nonnull String individualIri,
                                 @Nullable Map<String, String> properties,
                                 long updatedAt,
                                 @Nullable String updatedBy) {
        this(projectId, individualIri, properties, updatedAt, updatedBy, null);
    }

    @Nonnull
    @JsonProperty("projectId")
    public String getProjectId() {
        return projectId;
    }

    @Nonnull
    @JsonProperty("individualIri")
    public String getIndividualIri() {
        return individualIri;
    }

    @Nonnull
    @JsonProperty("properties")
    public Map<String, String> getProperties() {
        return properties;
    }

    @Nonnull
    @JsonProperty("types")
    public List<String> getTypes() {
        return types;
    }

    @JsonProperty("updatedAt")
    public long getUpdatedAt() {
        return updatedAt;
    }

    @Nullable
    @JsonProperty("updatedBy")
    public String getUpdatedBy() {
        return updatedBy;
    }

    @Nonnull
    public IndividualRuntimeData withProperties(@Nonnull Map<String, String> newProperties,
                                                long newUpdatedAt,
                                                @Nullable String newUpdatedBy) {
        return new IndividualRuntimeData(projectId, individualIri, newProperties, newUpdatedAt, newUpdatedBy, types);
    }

    @Nonnull
    private static List<String> copyIris(@Nullable List<String> iris) {
        if (iris == null || iris.isEmpty()) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(new ArrayList<>(iris));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IndividualRuntimeData)) {
            return false;
        }
        IndividualRuntimeData that = (IndividualRuntimeData) o;
        return updatedAt == that.updatedAt
                && projectId.equals(that.projectId)
                && individualIri.equals(that.individualIri)
                && properties.equals(that.properties)
                && types.equals(that.types)
                && Objects.equals(updatedBy, that.updatedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectId, individualIri, properties, types, updatedAt, updatedBy);
    }

    @Override
    public String toString() {
        return "IndividualRuntimeData{" +
                "projectId='" + projectId + '\'' +
                ", individualIri='" + individualIri + '\'' +
                ", types=" + types +
                ", properties=" + properties +
                ", updatedAt=" + updatedAt +
                ", updatedBy='" + updatedBy + '\'' +
                '}';
    }
}
