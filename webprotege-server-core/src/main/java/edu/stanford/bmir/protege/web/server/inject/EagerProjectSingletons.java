package edu.stanford.bmir.protege.web.server.inject;

import edu.stanford.bmir.protege.web.server.dispatch.impl.ProjectActionHandlerRegistry;
import edu.stanford.bmir.protege.web.server.revision.RevisionManager;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLOntology;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-02
 *
 * This isn't used for anything other than to force the eager instantiation of certain objects
 * in the project component object graph.
 */
@ProjectSingleton
public class EagerProjectSingletons {

    private final ProjectId projectId;

    private final RevisionManager revisionManager;

    private final OWLOntology rootOntology;

    private final ProjectActionHandlerRegistry projectActionHandlerRegistry;

    @Inject
    public EagerProjectSingletons(ProjectId projectId,
                                  RevisionManager revisionManager,
                                  OWLOntology rootOntology,
                                  ProjectActionHandlerRegistry projectActionHandlerRegistry) {
        this.projectId = projectId;
        this.revisionManager = revisionManager;
        this.rootOntology = rootOntology;
        this.projectActionHandlerRegistry = projectActionHandlerRegistry;
    }
}
