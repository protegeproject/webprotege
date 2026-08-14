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
 * Read-only OWL class frame for third-party schema discovery.
 */
public class OntologyClassData {

    @Nonnull
    private final String projectId;

    @Nonnull
    private final String classIri;

    @Nonnull
    private final List<String> parents;

    @Nonnull
    private final Map<String, String> properties;

    @JsonCreator
    public OntologyClassData(@JsonProperty("projectId") @Nonnull String projectId,
                             @JsonProperty("classIri") @Nonnull String classIri,
                             @JsonProperty("parents") @Nullable List<String> parents,
                             @JsonProperty("properties") @Nullable Map<String, String> properties) {
        this.projectId = Objects.requireNonNull(projectId, "projectId");
        this.classIri = Objects.requireNonNull(classIri, "classIri");
        this.parents = copyList(parents);
        this.properties = properties == null
                ? Collections.emptyMap()
                : Collections.unmodifiableMap(new LinkedHashMap<>(properties));
    }

    @Nonnull
    @JsonProperty("projectId")
    public String getProjectId() {
        return projectId;
    }

    @Nonnull
    @JsonProperty("classIri")
    public String getClassIri() {
        return classIri;
    }

    @Nonnull
    @JsonProperty("parents")
    public List<String> getParents() {
        return parents;
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
        if (!(o instanceof OntologyClassData)) {
            return false;
        }
        OntologyClassData that = (OntologyClassData) o;
        return projectId.equals(that.projectId)
                && classIri.equals(that.classIri)
                && parents.equals(that.parents)
                && properties.equals(that.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectId, classIri, parents, properties);
    }

    @Nonnull
    private static List<String> copyList(@Nullable List<String> values) {
        if (values == null || values.isEmpty()) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(new ArrayList<>(values));
    }
}
