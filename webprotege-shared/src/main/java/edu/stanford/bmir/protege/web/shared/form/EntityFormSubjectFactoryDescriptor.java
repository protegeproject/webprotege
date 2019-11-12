package edu.stanford.bmir.protege.web.shared.form;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-11
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class EntityFormSubjectFactoryDescriptor {

    @JsonCreator
    public static EntityFormSubjectFactoryDescriptor get(@Nonnull @JsonProperty("entityType") EntityType entityType,
                                                         @Nonnull @JsonProperty("suppliedNameTemplate") String suppliedNameTemplate,
                                                         @Nonnull @JsonProperty("axiomTemplates") ImmutableList<String> axiomTemplates) {
        return new AutoValue_Entity_FormSubjectFactoryDescriptor(entityType, suppliedNameTemplate, axiomTemplates);
    }

    @Nonnull
    public abstract EntityType getEntityType();

    /**
     * Gets the supplied template for generating what would be tha
     * user supplied name for an entity.  The generation of the entity IRI
     * is based on this supplied name and the WebProtege entity creation settings.
     *
     * The following template variables can be used:
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
    public abstract String getSuppliedNameTemplate();

    /**
     * A list of axioms templates, in functional syntax.  Each entry must be
     * a single axiom written in functional syntax without abbreviations.
     * Axiom templates can be used to "position" the fresh entity in a
     * hierarchy or as an instance of a class.  The variable ${subject.iri} should be
     * used to refer to the subject IRI.  For example,
     * SubClassOf(${subject.iri} &lt;http://example.org/A&gt;)
     * @return A, possibly empty list of axiom templates.
     */
    @Nonnull
    public abstract ImmutableList<String> getAxiomTemplates();
}
