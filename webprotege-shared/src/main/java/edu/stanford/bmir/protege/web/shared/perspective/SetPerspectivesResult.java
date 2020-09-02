package edu.stanford.bmir.protege.web.shared.perspective;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18/02/16
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class SetPerspectivesResult implements Result {

    @Nonnull
    public abstract ImmutableList<PerspectiveDescriptor> getPerspectives();

    @Nonnull
    public abstract ImmutableSet<PerspectiveId> getResettablePerspectives();

    @Nonnull
    public static SetPerspectivesResult get(@Nonnull ImmutableList<PerspectiveDescriptor> perspectives,
                                            @Nonnull ImmutableSet<PerspectiveId> resettablePerspectives) {
        return new AutoValue_SetPerspectivesResult(perspectives, resettablePerspectives);
    }

}
