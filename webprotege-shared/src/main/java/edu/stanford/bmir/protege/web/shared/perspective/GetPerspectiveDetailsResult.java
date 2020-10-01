package edu.stanford.bmir.protege.web.shared.perspective;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-03
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class GetPerspectiveDetailsResult implements Result {

    @Nonnull
    public static GetPerspectiveDetailsResult get(@Nonnull ImmutableList<PerspectiveDetails> perspectiveDetails) {
        return new AutoValue_GetPerspectiveDetailsResult(perspectiveDetails);
    }

    @Nonnull
    public abstract ImmutableList<PerspectiveDetails> getPerspectiveDetails();
}
