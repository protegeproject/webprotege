package edu.stanford.bmir.protege.web.client.project;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/12/15
 *
 * Manages that project that is active in the user interface.  If the active project
 * changes then an {@link edu.stanford.bmir.protege.web.client.project.ActiveProjectChangedEvent}
 * is fired on the event bus.
 */
public class ActiveProjectManagerImpl implements ActiveProjectManager {

    private final EventBus eventBus;

    private Optional<ProjectId> activeProject = Optional.absent();

    @Inject
    public ActiveProjectManagerImpl(EventBus eventBus) {
        this.eventBus = checkNotNull(eventBus);
    }

    @Override
    public Optional<ProjectId> getActiveProjectId() {
        return activeProject;
    }

    @Override
    public void setActiveProject(Optional<ProjectId> project) {
        checkNotNull(project);
        if(!project.equals(activeProject)) {
            GWT.log("[ActiveProjectManagerImpl] Active project changed to " + project);
            activeProject = project;
            eventBus.fireEvent(new ActiveProjectChangedEvent(activeProject));
        }
    }
}
