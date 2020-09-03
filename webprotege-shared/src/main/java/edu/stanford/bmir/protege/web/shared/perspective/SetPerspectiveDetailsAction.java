package edu.stanford.bmir.protege.web.shared.perspective;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-03
 */
public class SetPerspectiveDetailsAction implements ProjectAction<SetPerspectiveDetailsResult> {

    private ProjectId projectId;

    private ImmutableList<PerspectiveDetails> perspectiveDetails;

    public SetPerspectiveDetailsAction(@Nonnull ProjectId projectId,
                                       @Nonnull ImmutableList<PerspectiveDetails> perspectiveDetails) {
        this.projectId = checkNotNull(projectId);
        this.perspectiveDetails = checkNotNull(perspectiveDetails);
    }

    @GwtSerializationConstructor
    private SetPerspectiveDetailsAction() {
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    public ImmutableList<PerspectiveDetails> getPerspectiveDetails() {
        return perspectiveDetails;
    }
}
