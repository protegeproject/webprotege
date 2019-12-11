package edu.stanford.bmir.protege.web.shared.viz;

import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-10
 */
@AutoValue
public abstract class SplitEdgeCriteria {

    public static SplitEdgeCriteria get(@Nonnull CompositeEdgeCriteria positiveCriteria,
                                        @Nonnull CompositeEdgeCriteria negativeCriteria) {
        return new AutoValue_SplitEdgeCriteria(positiveCriteria, negativeCriteria);
    }

    @Nonnull
    public abstract CompositeEdgeCriteria getInclusionCriteria();

    @Nonnull
    public abstract CompositeEdgeCriteria getExclusionCriteria();

}
