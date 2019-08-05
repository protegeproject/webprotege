package edu.stanford.bmir.protege.web.server.project;

import edu.stanford.bmir.protege.web.server.dispatch.impl.ProjectActionHandlerRegistry;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.server.revision.RevisionManager;
import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.event.EventTag;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.event.ProjectEventList;
import edu.stanford.bmir.protege.web.shared.project.NewProjectSettings;
import edu.stanford.bmir.protege.web.shared.project.ProjectAlreadyExistsException;
import edu.stanford.bmir.protege.web.shared.project.ProjectDocumentNotFoundException;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.IOException;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 07/03/2012
 */
public class ProjectManager {

    private final ProjectCache projectCache;

    private final ProjectAccessManager projectAccessManager;

    @Inject
    public ProjectManager(@Nonnull ProjectCache projectCache,
                          @Nonnull ProjectAccessManager projectAccessManager) {
        this.projectCache = projectCache;
        this.projectAccessManager = projectAccessManager;
    }


    @Nonnull
    public ProjectActionHandlerRegistry getActionHandlerRegistry(@Nonnull ProjectId projectId) {
        return projectCache.getActionHandlerRegistry(checkNotNull(projectId));
    }

    /**
     * Requests that the specified project is loaded for the specified user.
     * @param projectId The project.
     * @param requestingUser The user that is requesting that the project is loaded.
     * @throws ProjectDocumentNotFoundException If there is no such project.
     */
    public void ensureProjectIsLoaded(@Nonnull ProjectId projectId,
                                      @Nonnull UserId requestingUser) throws ProjectDocumentNotFoundException {
        long currentTime = System.currentTimeMillis();
        projectAccessManager.logProjectAccess(projectId, requestingUser, currentTime);
        projectCache.ensureProjectIsLoaded(projectId);
    }

    public RevisionManager getRevisionManager(@Nonnull ProjectId projectId) {
        return projectCache.getRevisionManager(projectId);
    }

    public ProjectId createNewProject(@Nonnull NewProjectSettings newProjectSettings) throws ProjectAlreadyExistsException, OWLOntologyCreationException, IOException, OWLOntologyStorageException {
        return projectCache.getProject(newProjectSettings);
    }

    /**
     * Gets the events for the specified project, if the project is active.
     * @param projectId The project id
     * @param sinceTag The event tag from which events should be retrieved
     * @return A, possibly empty, event list
     */
    @Nonnull
    public ProjectEventList getProjectEventsSinceTag(@Nonnull ProjectId projectId,
                                                     @Nonnull EventTag sinceTag) {
        Optional<EventManager<ProjectEvent<?>>> pem = projectCache.getProjectEventManagerIfActive(projectId);
        if(pem.isEmpty()) {
            return getEmptyProjectEventList(projectId, sinceTag);
        }
        EventManager<ProjectEvent<?>> eventManager = pem.get();
        EventList<ProjectEvent<?>> eventList = eventManager.getEventsFromTag(sinceTag);
        return ProjectEventList.builder(eventList.getStartTag(), projectId, eventList.getEndTag()).addEvents(eventList.getEvents()).build();

    }

    private static ProjectEventList getEmptyProjectEventList(@Nonnull ProjectId projectId,
                                                             @Nonnull EventTag sinceTag) {
        return ProjectEventList.builder(sinceTag, projectId, sinceTag).build();
    }
}
