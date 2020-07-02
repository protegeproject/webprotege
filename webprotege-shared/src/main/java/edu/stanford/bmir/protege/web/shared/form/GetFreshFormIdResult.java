package edu.stanford.bmir.protege.web.shared.form;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.project.HasProjectId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-16
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class GetFreshFormIdResult implements Result, HasProjectId {

    public static GetFreshFormIdResult get(@Nonnull ProjectId projectId,
                                           @Nonnull FormId formId) {
        return new AutoValue_GetFreshFormIdResult(projectId, formId);
    }

    @Nonnull
    public abstract ProjectId getProjectId();

    @Nonnull
    public abstract FormId getFormId();
}
