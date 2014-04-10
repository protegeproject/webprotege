package edu.stanford.bmir.protege.web.shared.event;

import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/04/2013
 */
public class ProjectMovedToTrashEvent extends WebProtegeEvent<ProjectMovedToTrashHandler> implements HasProjectId {

    public static final transient Type<ProjectMovedToTrashHandler> TYPE = new Type<ProjectMovedToTrashHandler>();

    private ProjectId projectId;

    private ProjectMovedToTrashEvent() {
    }

    public ProjectMovedToTrashEvent(ProjectId projectId) {
        this.projectId = projectId;
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    @Override
    public Type<ProjectMovedToTrashHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ProjectMovedToTrashHandler handler) {
        handler.handleProjectMovedToTrash(this);
    }
}
