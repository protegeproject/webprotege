package edu.stanford.bmir.protege.web.shared.viz;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.match.criteria.EntityMatchCriteria;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-06
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("TailNodeMatches")
public abstract class TailNodeMatchesCriteria implements NodeMatchesCriteria {

    public static TailNodeMatchesCriteria get(@Nonnull EntityMatchCriteria criteria) {
        return new AutoValue_TailNodeMatchesCriteria(criteria);
    }

    @Override
    public <R> R accept(@Nonnull EdgeCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
