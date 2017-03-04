package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.OWLOntology;

import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/04/2012
 */
public abstract class OntologyServiceStrategy<R> {

    private Project project;

    private UserId userId;

    protected OntologyServiceStrategy(Project project, UserId userId) {
        this.project = project;
        this.userId = userId;
    }

    public Project getProject() {
        return project;
    }

    public RenderingManager getRenderingManager() {
        return getProject().getRenderingManager();
    }

    public OWLOntology getRootOntology() {
        return getProject().getRootOntology();
    }


    public Set<OWLOntology> getRootOntologyImportsClosure() {
        return getRootOntology().getImportsClosure();
    }

    public UserId getUserId() {
        return userId;
    }

    public abstract R execute();
}
