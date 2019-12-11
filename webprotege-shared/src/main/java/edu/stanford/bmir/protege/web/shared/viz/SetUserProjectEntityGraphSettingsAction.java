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

    private EdgeCriteria edgeCriteria;

    private double rankSeparation;

    public SetUserProjectEntityGraphSettingsAction(@Nonnull ProjectId projectId,
                                                   @Nonnull EdgeCriteria criteria, double rankSeparation) {
        this.projectId = checkNotNull(projectId);
        this.edgeCriteria = checkNotNull(criteria);
        this.rankSeparation = rankSeparation;
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
    public EdgeCriteria getEdgeCriteria() {
        return edgeCriteria;
    }

    public double getRankSeparation() {
        return rankSeparation;
    }
}
