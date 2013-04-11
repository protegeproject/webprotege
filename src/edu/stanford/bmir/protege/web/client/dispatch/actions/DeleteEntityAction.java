package edu.stanford.bmir.protege.web.client.dispatch.actions;

import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.HasSubject;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/02/2013
 */
public class DeleteEntityAction extends AbstractHasProjectIdAndSubject<OWLEntity> implements Action<DeleteEntityResult>, HasProjectId, HasSubject<OWLEntity> {

    private DeleteEntityAction() {
    }

    public DeleteEntityAction(OWLEntity subject, ProjectId projectId) {
        super(subject, projectId);
    }
}
