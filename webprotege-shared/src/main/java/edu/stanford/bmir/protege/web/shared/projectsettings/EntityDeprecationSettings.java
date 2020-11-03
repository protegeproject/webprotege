package edu.stanford.bmir.protege.web.shared.projectsettings;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-10-23
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class EntityDeprecationSettings {


    public static final String DEPRECATED_CLASSES_PARENT = "deprecatedClassesParent";

    public static final String DEPRECATED_OBJECT_PROPERTIES_PARENT = "deprecatedObjectPropertiesParent";

    public static final String DEPRECATED_DATA_PROPERTIES_PARENT = "deprecatedDataPropertiesParent";

    public static final String DEPRECATED_ANNOTATION_PROPERTIES_PARENT = "deprecatedAnnotationPropertiesParent";

    public static final String DEPRECATED_INDIVIDUALS_PARENT = "deprecatedIndividualsParent";

    public static final String REPLACED_BY_PROPERTY_IRI = "replacedByPropertyIri";

    @JsonProperty(REPLACED_BY_PROPERTY_IRI)
    @Nullable
    abstract IRI getReplacedByPropertyIriInternal();

    @JsonIgnore
    @Nonnull
    public Optional<IRI> getReplacedByPropertyIri() {
        return Optional.ofNullable(getReplacedByPropertyIriInternal());
    }


    @JsonProperty(DEPRECATED_CLASSES_PARENT)
    @Nullable
    abstract OWLClass getDeprecatedClassesParentInternal();

    @JsonIgnore
    @Nonnull
    public Optional<OWLClass> getDeprecatedClassesParent() {
        return Optional.ofNullable(getDeprecatedClassesParentInternal());
    }

    @JsonProperty(DEPRECATED_OBJECT_PROPERTIES_PARENT)
    @Nullable
    abstract OWLObjectProperty getDeprecatedObjectPropertiesParentInternal();

    @JsonIgnore
    @Nonnull
    public Optional<OWLObjectProperty> getDeprecatedObjectPropertiesParent() {
        return Optional.ofNullable(getDeprecatedObjectPropertiesParentInternal());
    }

    @JsonProperty(DEPRECATED_DATA_PROPERTIES_PARENT)
    @Nullable
    abstract OWLDataProperty getDeprecatedDataPropertiesParentInternal();

    @JsonIgnore
    @Nonnull
    public Optional<OWLDataProperty> getDeprecatedDataPropertiesParent() {
        return Optional.ofNullable(getDeprecatedDataPropertiesParentInternal());
    }

    @JsonProperty(DEPRECATED_ANNOTATION_PROPERTIES_PARENT)
    @Nullable
    abstract OWLAnnotationProperty getDeprecatedAnnotationPropertiesParentInternal();

    @JsonIgnore
    @Nonnull
    public Optional<OWLAnnotationProperty> getDeprecatedAnnotationPropertiesParent() {
        return Optional.ofNullable(getDeprecatedAnnotationPropertiesParentInternal());
    }

    @JsonProperty(DEPRECATED_INDIVIDUALS_PARENT)
    @Nullable
    abstract OWLClass getDeprecatedIndividualsParentInternal();

    @JsonIgnore
    @Nonnull
    public Optional<OWLClass> getDeprecatedIndividualsParent() {
        return Optional.ofNullable(getDeprecatedIndividualsParentInternal());
    }

    @Nonnull
    public static EntityDeprecationSettings empty() {
        return get(null, null, null, null, null, null);
    }

    @Nonnull
    @JsonCreator
    public static EntityDeprecationSettings get(@JsonProperty(REPLACED_BY_PROPERTY_IRI) @Nullable IRI replacedByPropertyIri,
                                                @JsonProperty(DEPRECATED_CLASSES_PARENT) @Nullable OWLClass deprecatedClassesParent,
                                                @JsonProperty(DEPRECATED_OBJECT_PROPERTIES_PARENT) OWLObjectProperty deprecatedObjectPropertiesParent,
                                                @JsonProperty(DEPRECATED_DATA_PROPERTIES_PARENT) @Nullable OWLDataProperty deprecatedDataPropertiesParent,
                                                @JsonProperty(DEPRECATED_ANNOTATION_PROPERTIES_PARENT) @Nullable OWLAnnotationProperty deprecatedAnnotationPropertiesParent,
                                                @JsonProperty(DEPRECATED_INDIVIDUALS_PARENT) @Nullable OWLClass deprecatedIndividualsParent) {
        return new AutoValue_EntityDeprecationSettings(replacedByPropertyIri,
                                                       deprecatedClassesParent,
                                                       deprecatedObjectPropertiesParent,
                                                       deprecatedDataPropertiesParent,
                                                       deprecatedAnnotationPropertiesParent,
                                                       deprecatedIndividualsParent);
    }
}
