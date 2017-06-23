package edu.stanford.bmir.protege.web.client.dispatch;

import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 */
public abstract class AbstractHasProjectAction<R extends Result> implements ProjectAction<R> {

    private ProjectId projectId;


    /**
     * For serialization purposes only
     */
    protected AbstractHasProjectAction() {

    }

    public AbstractHasProjectAction(ProjectId projectId) {
        this.projectId = checkNotNull(projectId);
    }

    /**
     * Get the {@link edu.stanford.bmir.protege.web.shared.project.ProjectId}.
     * @return The {@link edu.stanford.bmir.protege.web.shared.project.ProjectId}.  Not {@code null}.
     */
    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }
}
