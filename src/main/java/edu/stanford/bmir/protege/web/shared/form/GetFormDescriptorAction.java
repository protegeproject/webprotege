package edu.stanford.bmir.protege.web.shared.form;

import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.HasSubject;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public class GetFormDescriptorAction implements ProjectAction<GetFormDescriptorResult>, HasSubject<OWLEntity> {

    private ProjectId projectId;

    private OWLEntity subject;

    private GetFormDescriptorAction() {
    }

    public GetFormDescriptorAction(ProjectId projectId, OWLEntity subject) {
        this.projectId = projectId;
        this.subject = subject;
    }

    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Override
    public OWLEntity getSubject() {
        return subject;
    }
}
