package edu.stanford.bmir.protege.web.shared.project;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10/03/16
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class GetProjectDetailsResult implements Result {

    public static GetProjectDetailsResult get(@Nonnull ProjectDetails projectDetails) {
        return new AutoValue_GetProjectDetailsResult(projectDetails);
    }

    @Nonnull
    public abstract ProjectDetails getProjectDetails();
}
