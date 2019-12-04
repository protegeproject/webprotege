package edu.stanford.bmir.protege.web.shared.match.criteria;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import org.semanticweb.owlapi.model.OWLPrimitive;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-02
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("ValueEqualTo")
public abstract class RelationshipValueThatIsEqualToCriteria implements RelationshipValueCriteria {

    private static final String VALUE = "value";

    public static RelationshipValueThatIsEqualToCriteria get(@Nonnull OWLPrimitive value) {
        return new AutoValue_RelationshipValueThatIsEqualToCriteria(value);
    }

    @Nonnull
    @JsonProperty(VALUE)
    public abstract OWLPrimitive getValue();


    @Override
    public <R> R accept(@Nonnull RelationshipValueCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
