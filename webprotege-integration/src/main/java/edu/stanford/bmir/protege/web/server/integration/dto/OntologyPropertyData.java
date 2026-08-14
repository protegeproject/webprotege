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
 * Read-only OWL property frame for third-party schema discovery.
 */
public class OntologyPropertyData {

    @Nonnull
    private final String projectId;

    @Nonnull
    private final String propertyIri;

    @Nonnull
    private final OntologyPropertyKind kind;

    @Nonnull
    private final List<String> domains;

    @Nonnull
    private final List<String> ranges;

    @Nonnull
    private final List<String> characteristics;

    @Nonnull
    private final List<String> inverses;

    @Nonnull
    private final Map<String, String> annotations;

    @JsonCreator
    public OntologyPropertyData(@JsonProperty("projectId") @Nonnull String projectId,
                                @JsonProperty("propertyIri") @Nonnull String propertyIri,
                                @JsonProperty("kind") @Nonnull OntologyPropertyKind kind,
                                @JsonProperty("domains") @Nullable List<String> domains,
                                @JsonProperty("ranges") @Nullable List<String> ranges,
                                @JsonProperty("characteristics") @Nullable List<String> characteristics,
                                @JsonProperty("inverses") @Nullable List<String> inverses,
                                @JsonProperty("annotations") @Nullable Map<String, String> annotations) {
        this.projectId = Objects.requireNonNull(projectId, "projectId");
        this.propertyIri = Objects.requireNonNull(propertyIri, "propertyIri");
        this.kind = Objects.requireNonNull(kind, "kind");
        this.domains = copyList(domains);
        this.ranges = copyList(ranges);
        this.characteristics = copyList(characteristics);
        this.inverses = copyList(inverses);
        this.annotations = annotations == null
                ? Collections.emptyMap()
                : Collections.unmodifiableMap(new LinkedHashMap<>(annotations));
    }

    @Nonnull
    @JsonProperty("projectId")
    public String getProjectId() {
        return projectId;
    }

    @Nonnull
    @JsonProperty("propertyIri")
    public String getPropertyIri() {
        return propertyIri;
    }

    @Nonnull
    @JsonProperty("kind")
    public OntologyPropertyKind getKind() {
        return kind;
    }

    @Nonnull
    @JsonProperty("domains")
    public List<String> getDomains() {
        return domains;
    }

    @Nonnull
    @JsonProperty("ranges")
    public List<String> getRanges() {
        return ranges;
    }

    @Nonnull
    @JsonProperty("characteristics")
    public List<String> getCharacteristics() {
        return characteristics;
    }

    @Nonnull
    @JsonProperty("inverses")
    public List<String> getInverses() {
        return inverses;
    }

    @Nonnull
    @JsonProperty("annotations")
    public Map<String, String> getAnnotations() {
        return annotations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OntologyPropertyData)) {
            return false;
        }
        OntologyPropertyData that = (OntologyPropertyData) o;
        return projectId.equals(that.projectId)
                && propertyIri.equals(that.propertyIri)
                && kind == that.kind
                && domains.equals(that.domains)
                && ranges.equals(that.ranges)
                && characteristics.equals(that.characteristics)
                && inverses.equals(that.inverses)
                && annotations.equals(that.annotations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectId, propertyIri, kind, domains, ranges, characteristics, inverses, annotations);
    }

    @Nonnull
    private static List<String> copyList(@Nullable List<String> values) {
        if (values == null || values.isEmpty()) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(new ArrayList<>(values));
    }
}
