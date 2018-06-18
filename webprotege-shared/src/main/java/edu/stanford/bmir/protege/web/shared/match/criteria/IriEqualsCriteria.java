package edu.stanford.bmir.protege.web.shared.match.criteria;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Jun 2018
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("IriEquals")
public abstract class IriEqualsCriteria implements IriCriteria, AnnotationPropertyCriteria {

    private static final String IRI_PROPERTY = "iri";

    @Nonnull
    @JsonProperty(IRI_PROPERTY)
    public abstract IRI getIri();

    @Nonnull
    @JsonCreator
    public static IriEqualsCriteria get(@Nonnull @JsonProperty(IRI_PROPERTY) IRI iri) {
        return new AutoValue_IriEqualsCriteria(iri);
    }

    @Nonnull
    public static IriEqualsCriteria iriEqualTo(@Nonnull String iri) {
        return get(IRI.create(iri));
    }

    @Nonnull
    public static IriEqualsCriteria isRdfsLabel() {
        return get(OWLRDFVocabulary.RDFS_LABEL.getIRI());
    }

    @Nonnull
    public static IriEqualsCriteria isOwlDeprecated() {
        return get(OWLRDFVocabulary.OWL_DEPRECATED.getIRI());
    }

    @Nonnull
    public static IriEqualsCriteria get(@Nonnull OWLAnnotationProperty property) {
        return get(property.getIRI());
    }

    @Override
    public <R> R accept(@Nonnull AnnotationPropertyCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public <R> R accept(@Nonnull AnnotationValueCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
