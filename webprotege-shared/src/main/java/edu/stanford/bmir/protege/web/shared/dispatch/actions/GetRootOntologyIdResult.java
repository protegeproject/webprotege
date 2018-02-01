package edu.stanford.bmir.protege.web.shared.dispatch.actions;

import edu.stanford.bmir.protege.web.shared.project.HasProjectId;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 */
public class GetRootOntologyIdResult implements Result, HasProjectId {

    private ProjectId projectId;

    private OWLOntologyID ontologyID;

    /**
     * For serialization only
     */
    private GetRootOntologyIdResult() {
    }

    public GetRootOntologyIdResult(ProjectId projectId, OWLOntologyID owlOntologyID) {
        this.projectId = projectId;
        this.ontologyID = owlOntologyID;
    }

    /**
     * Get the {@link edu.stanford.bmir.protege.web.shared.project.ProjectId}.
     *
     * @return The {@link edu.stanford.bmir.protege.web.shared.project.ProjectId}.  Not {@code null}.
     */
    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    public OWLOntologyID getObject() {
        return ontologyID;
    }
}
