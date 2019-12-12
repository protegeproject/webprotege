package edu.stanford.bmir.protege.web.shared.viz;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.projectsettings.GetProjectSettingsResult;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-10
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class GetUserProjectEntityGraphCriteriaResult implements Result {


    public static GetUserProjectEntityGraphCriteriaResult get(@Nonnull ProjectId projectId,
                                                              @Nonnull UserId userId,
                                                              @Nonnull EntityGraphSettings settings) {
        return new AutoValue_GetUserProjectEntityGraphCriteriaResult(projectId,
                                                                     userId,
                                                                     settings);
    }

    @Nonnull
    public abstract ProjectId getProjectId();

    @Nonnull
    public abstract UserId getUserId();

    @Nonnull
    public abstract EntityGraphSettings getSettings();

}
