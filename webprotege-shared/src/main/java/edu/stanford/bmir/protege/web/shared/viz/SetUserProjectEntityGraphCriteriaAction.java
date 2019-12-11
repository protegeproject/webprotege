package edu.stanford.bmir.protege.web.shared.viz;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-10
 */
public class SetUserProjectEntityGraphCriteriaAction implements ProjectAction<SetUserProjectEntityGraphCriteriaResult> {

    private ProjectId projectId;

    private EdgeCriteria edgeCriteria;

    public SetUserProjectEntityGraphCriteriaAction(@Nonnull ProjectId projectId,
                                                   @Nonnull EdgeCriteria criteria) {
        this.projectId = checkNotNull(projectId);
        this.edgeCriteria = checkNotNull(criteria);
    }

    @GwtSerializationConstructor
    private SetUserProjectEntityGraphCriteriaAction() {
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    public EdgeCriteria getEdgeCriteria() {
        return edgeCriteria;
    }
}
