package edu.stanford.bmir.protege.web.server.project;

import edu.stanford.bmir.protege.web.server.dispatch.impl.ProjectActionHandlerRegistry;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
import edu.stanford.bmir.protege.web.server.revision.RevisionManager;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLOntology;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/03/2012
 */
@Deprecated
@ProjectSingleton
public class Project {

    private final ProjectId projectId;

    private final EventManager<ProjectEvent<?>> projectEventManager;

    private final OWLOntology ontology;

    private final RevisionManager changeManager;

    @Nonnull
    private final ProjectActionHandlerRegistry actionHandlerRegistry;

    @Inject
    public Project(ProjectId projectId,
                   EventManager<ProjectEvent<?>> projectEventManager,
                   @RootOntology OWLOntology ontology,
                   RevisionManager changeManager,
                   @Nonnull ProjectActionHandlerRegistry actionHandlerRegistry) {
        this.projectId = projectId;
        this.projectEventManager = projectEventManager;
        this.ontology = ontology;
        this.changeManager = changeManager;
        this.actionHandlerRegistry = actionHandlerRegistry;
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    public EventManager<ProjectEvent<?>> getEventManager() {
        return projectEventManager;
    }

    public RevisionManager getRevisionManager() {
        return changeManager;
    }

    public OWLOntology getRootOntology() {
        return ontology;
    }

    public ProjectActionHandlerRegistry getActionHanderRegistry() {
        return actionHandlerRegistry;
    }
}
