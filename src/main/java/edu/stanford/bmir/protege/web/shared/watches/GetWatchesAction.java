package edu.stanford.bmir.protege.web.shared.watches;

import edu.stanford.bmir.protege.web.shared.HasUserId;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.OWLEntity;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/03/2013
 */
public class GetWatchesAction implements ProjectAction<GetWatchesResult>, HasUserId {

    private ProjectId projectId;

    private UserId userId;

    private OWLEntity entity;

    /**
     * Creates a {@link GetWatchesAction} object for the specified project and user and entity.
     * @param projectId The {@link ProjectId} of the project whose watches are to be retrieved.
     * @param userId The {@link UserId} of the user whose watches are to be retrieved in the specified project.
     * @param entity The entity.
     * @throws NullPointerException if any parameters are {@code null}.
     */
    public GetWatchesAction(ProjectId projectId, UserId userId, OWLEntity entity) {
        this.projectId = checkNotNull(projectId);
        this.userId = checkNotNull(userId);
        this.entity = entity;
    }

    /**
     * For serialization only
     */
    private GetWatchesAction() {
    }

    /**
     * Gets the {@link ProjectId}.
     * @return The {@link ProjectId}.  Not {@code null}.
     */
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    /**
     * Gets the {@link UserId}.
     * @return The {@link UserId}.  Not {@code null}.
     */
    @Override
    public UserId getUserId() {
        return userId;
    }

    public OWLEntity getEntity() {
        return entity;
    }
}
