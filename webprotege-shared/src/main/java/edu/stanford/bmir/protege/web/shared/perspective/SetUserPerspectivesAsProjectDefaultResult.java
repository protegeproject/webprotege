package edu.stanford.bmir.protege.web.shared.perspective;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-02
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class SetUserPerspectivesAsProjectDefaultResult implements Result {


    public static SetUserPerspectivesAsProjectDefaultResult get() {
        return new AutoValue_SetUserPerspectivesAsProjectDefaultResult();
    }
}
