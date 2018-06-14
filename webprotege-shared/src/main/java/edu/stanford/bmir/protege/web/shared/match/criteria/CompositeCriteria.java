package edu.stanford.bmir.protege.web.shared.match.criteria;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Jun 2018
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("Composite")
public abstract class CompositeCriteria<C extends Criteria> implements Criteria {

    public abstract ImmutableList<C> getCriteria();

    public static <C extends Criteria> CompositeCriteria<? extends C> get(@Nonnull ImmutableList<? extends C> criteria) {
        return new AutoValue_CompositeCriteria<>(criteria);
    }

}
