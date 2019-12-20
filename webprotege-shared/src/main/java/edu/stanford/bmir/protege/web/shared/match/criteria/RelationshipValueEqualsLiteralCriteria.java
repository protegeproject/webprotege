package edu.stanford.bmir.protege.web.shared.match.criteria;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-04
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("ValueEqualToLiteral")
public abstract class RelationshipValueEqualsLiteralCriteria implements RelationshipValueEqualsCriteria {

    @Nonnull
    @JsonCreator
    public static RelationshipValueEqualsLiteralCriteria get(@Nonnull @JsonProperty("value") OWLLiteral literal) {
        return new AutoValue_RelationshipValueEqualsLiteralCriteria(literal);
    }

    @Nonnull
    @Override
    public abstract OWLLiteral getValue();

    @Override
    public <R> R accept(@Nonnull RelationshipValueCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
