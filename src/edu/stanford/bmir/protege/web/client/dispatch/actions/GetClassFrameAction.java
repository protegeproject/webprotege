package edu.stanford.bmir.protege.web.client.dispatch.actions;

import edu.stanford.bmir.protege.web.client.dispatch.Cachable;
import edu.stanford.bmir.protege.web.client.ui.frame.LabelledFrame;
import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.HasSubject;
import edu.stanford.bmir.protege.web.shared.dispatch.GetObjectAction;
import edu.stanford.bmir.protege.web.shared.frame.ClassFrame;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLClass;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 */
public class GetClassFrameAction extends AbstractHasProjectIdAndSubject<OWLClass> implements GetObjectAction<LabelledFrame<ClassFrame>>, HasProjectId, HasSubject<OWLClass>, Cachable {

    /**
     * For serialization purposes only
     */
    private GetClassFrameAction() {

    }

    public GetClassFrameAction(OWLClass subject, ProjectId projectId) {
        super(subject, projectId);
    }

    @Override
    public int hashCode() {
        return "GetClassFrameAction".hashCode() + super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof GetClassFrameAction)) {
            return false;
        }
        GetClassFrameAction other = (GetClassFrameAction) obj;
        return super.equals(other);
    }
}
