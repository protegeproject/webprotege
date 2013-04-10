package edu.stanford.bmir.protege.web.client.dispatch.actions;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.client.ui.frame.LabelledFrame;
import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.HasSubject;
import edu.stanford.bmir.protege.web.shared.dispatch.GetObjectAction;
import edu.stanford.bmir.protege.web.shared.frame.NamedIndividualFrame;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 */
public class GetNamedIndividualFrameAction extends AbstractHasProjectIdAndSubject<OWLNamedIndividual> implements GetObjectAction<LabelledFrame<NamedIndividualFrame>>, HasProjectId, HasSubject<OWLNamedIndividual> {

    /**
     * For serialization purposes only
     */
    private GetNamedIndividualFrameAction() {
        super();
    }

    public GetNamedIndividualFrameAction(OWLNamedIndividual subject, ProjectId projectId) {
        super(subject, projectId);
    }


    @Override
    public int hashCode() {
        return "GetNamedIndividualFrameAction".hashCode() + super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof GetNamedIndividualFrameAction)) {
            return false;
        }
        GetNamedIndividualFrameAction other = (GetNamedIndividualFrameAction) obj;
        return super.equals(other);
    }
}
