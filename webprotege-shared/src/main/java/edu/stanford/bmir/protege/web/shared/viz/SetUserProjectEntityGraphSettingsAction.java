package edu.stanford.bmir.protege.web.shared.viz;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-10
 */
public class SetUserProjectEntityGraphSettingsAction implements ProjectAction<SetUserProjectEntityGraphResult> {

    private ProjectId projectId;

    private EntityGraphSettings settings;

    public SetUserProjectEntityGraphSettingsAction(@Nonnull ProjectId projectId,
                                                   @Nonnull EntityGraphSettings settings) {
        this.projectId = checkNotNull(projectId);
        this.settings = checkNotNull(settings);
    }

    @GwtSerializationConstructor
    private SetUserProjectEntityGraphSettingsAction() {
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    public EntityGraphSettings getSettings() {
        return settings;
    }
}
