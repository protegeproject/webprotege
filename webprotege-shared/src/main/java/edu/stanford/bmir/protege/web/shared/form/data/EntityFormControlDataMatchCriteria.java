package edu.stanford.bmir.protege.web.shared.form.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.match.criteria.EntityMatchCriteria;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-06-16
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class EntityFormControlDataMatchCriteria implements PrimitiveFormControlDataMatchCriteria {

    public static final String ENTITY_MATCHES = "entityMatches";

    public static EntityFormControlDataMatchCriteria get(@Nonnull EntityMatchCriteria entityMatchCriteria) {
        return new AutoValue_EntityFormControlDataMatchCriteria(entityMatchCriteria);
    }

    @JsonProperty(ENTITY_MATCHES)
    public abstract EntityMatchCriteria getEntityMatchCriteria();

    @Override
    public <R> R accept(@Nonnull PrimitiveFormControlDataMatchCriteriaVisitor<R> visitor) {
        return visitor.visit(this);
    }
}


