package edu.stanford.bmir.protege.web.shared.match.criteria;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLPrimitive;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-02
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("ValueEqualToEntity")
public abstract class RelationshipValueEqualsEntityCriteria implements RelationshipValueEqualsCriteria {

    private static final String VALUE = "value";

    @JsonCreator
    public static RelationshipValueEqualsEntityCriteria get(@Nonnull @JsonProperty(VALUE) OWLEntity value) {
        return new AutoValue_RelationshipValueEqualsEntityCriteria(value);
    }

    @Nonnull
    @JsonProperty(VALUE)
    public abstract OWLEntity getValue();


    @Override
    public <R> R accept(@Nonnull RelationshipValueCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
