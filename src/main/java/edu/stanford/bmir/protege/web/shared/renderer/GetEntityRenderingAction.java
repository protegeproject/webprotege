package edu.stanford.bmir.protege.web.shared.renderer;

import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 25/02/2014
 */
public class GetEntityRenderingAction implements ProjectAction<GetEntityRenderingResult> {

    private OWLEntity entity;

    private ProjectId projectId;



    private GetEntityRenderingAction() {
    }

    public GetEntityRenderingAction(ProjectId projectId, OWLEntity entity) {
        this.projectId = projectId;
        this.entity = entity;
    }

    @Nonnull
    public ProjectId getProjectId() {
        return projectId;
    }

    public OWLEntity getEntity() {
        return entity;
    }
}
