package edu.stanford.bmir.protege.web.shared.form;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-11
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class FormSubjectFactoryDescriptor {

    @JsonCreator
    public static FormSubjectFactoryDescriptor get(@Nonnull @JsonProperty("entityType") EntityType entityType,
                                                   @Nullable @JsonProperty("generatedNamePattern") String generatedNamePattern,
                                                   @Nullable @JsonProperty("parent") OWLClass parent,
                                                   @Nonnull @JsonProperty("targetOntologyIri") Optional<IRI> targetOntologyIri) {
        return new AutoValue_FormSubjectFactoryDescriptor(entityType,
                                                          generatedNamePattern == null ? "" : generatedNamePattern,
                                                          parent,
                                                          targetOntologyIri.orElse(null));
    }

    public static String getDefaultGeneratedNamePattern() {
        return "id-${uuid}";
    }

    @Nonnull
    public abstract EntityType getEntityType();

    /**
     * Gets the pattern for generating what would be tha
     * user supplied name for an entity.  The generation of the entity IRI
     * is based on this generated name and the WebProtege entity creation settings.
     *
     * The following pattern variables can be used:
     *
     *     ${type} - the short name of the type of entity being created, for example,
     *     "Class" for an owl:Class, "NamedIndividual" for an individual etc.
     *     ${uuid} – generates a fresh UUID.  For example, "MyClass-${uuid}" will
     *     generate a name of the form "MyClass-12345678-1234-1234-1234-123456789abc".
     *
     * If the supplied name is quoted IRI then this IRI will be used for the entity
     * and it will override any entity creation settings in WebProtege.
     */
    @Nonnull
    public abstract String getGeneratedNamePattern();

    /**
     * Gets a list of parents that can be used to position the fresh entity in
     * a hierarchy
     */
    @JsonIgnore
    @Nonnull
    public Optional<OWLClass> getParent() {
        return Optional.ofNullable(getParentInternal());
    }

    @JsonProperty("parent")
    @Nullable
    protected abstract OWLClass getParentInternal();

    @Nullable
    protected abstract IRI getTargetOntologyIriInternal();

    @Nonnull
    public Optional<IRI> getTargetOntologyIri() {
        return Optional.ofNullable(getTargetOntologyIriInternal());
    }
}
