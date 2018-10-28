package edu.stanford.bmir.protege.web.shared.dispatch;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27 Oct 2018
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class BatchResult implements Result {

    @Nonnull
    public static BatchResult get(@Nonnull ImmutableList<ActionExecutionResult> results) {
        return new AutoValue_BatchResult(results);
    }

    @Nonnull
    public abstract ImmutableList<ActionExecutionResult> getResults();
}
