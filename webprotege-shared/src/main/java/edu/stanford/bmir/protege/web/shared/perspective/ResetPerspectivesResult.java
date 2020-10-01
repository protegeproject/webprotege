package edu.stanford.bmir.protege.web.shared.perspective;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-03
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class ResetPerspectivesResult implements Result {

    public static ResetPerspectivesResult get() {
        return new AutoValue_ResetPerspectivesResult();
    }
}
