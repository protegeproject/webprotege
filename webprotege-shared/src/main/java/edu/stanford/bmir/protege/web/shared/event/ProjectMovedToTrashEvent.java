package edu.stanford.bmir.protege.web.shared.event;

import com.google.web.bindery.event.shared.Event;
import edu.stanford.bmir.protege.web.shared.project.HasProjectId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/04/2013
 */
public class ProjectMovedToTrashEvent extends WebProtegeEvent<ProjectMovedToTrashHandler> implements HasProjectId {

    public static final transient Event.Type<ProjectMovedToTrashHandler> ON_PROJECT_MOVED_TO_TRASH = new Event.Type<>();

    private ProjectId projectId;

    private ProjectMovedToTrashEvent() {
    }

    public ProjectMovedToTrashEvent(ProjectId projectId) {
        this.projectId = projectId;
    }

    @Nonnull
    public ProjectId getProjectId() {
        return projectId;
    }

    @Override
    public Event.Type<ProjectMovedToTrashHandler> getAssociatedType() {
        return ON_PROJECT_MOVED_TO_TRASH;
    }

    @Override
    protected void dispatch(ProjectMovedToTrashHandler handler) {
        handler.handleProjectMovedToTrash(this);
    }
}
