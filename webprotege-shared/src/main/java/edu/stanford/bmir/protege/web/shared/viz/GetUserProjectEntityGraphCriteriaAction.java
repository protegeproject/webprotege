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
public class GetUserProjectEntityGraphCriteriaAction implements ProjectAction<GetUserProjectEntityGraphCriteriaResult> {

    private ProjectId projectId;

    public GetUserProjectEntityGraphCriteriaAction(@Nonnull ProjectId projectId) {
        this.projectId = checkNotNull(projectId);
    }

    @GwtSerializationConstructor
    private GetUserProjectEntityGraphCriteriaAction() {
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }
}
