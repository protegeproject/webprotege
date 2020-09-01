package edu.stanford.bmir.protege.web.shared.perspective;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18/02/16
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class GetPerspectivesResult implements Result {

    @Nonnull
    public static GetPerspectivesResult get(ImmutableList<PerspectiveDescriptor> perspectives) {
        return new AutoValue_GetPerspectivesResult(perspectives);
    }

    @Nonnull
    public abstract ImmutableList<PerspectiveDescriptor> getPerspectives();
}
