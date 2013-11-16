package edu.stanford.bmir.protege.web.client.dispatch.actions;

import com.google.common.base.Objects;
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
public class GetClassFrameAction extends AbstractHasProjectIdAndSubject<OWLClass> implements GetObjectAction<LabelledFrame<ClassFrame>>, HasProjectId, HasSubject<OWLClass> {

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

    @Override
    public String toString() {
        return Objects.toStringHelper("GetClassFrameAction")
                .addValue(getProjectId())
                .add("entity", getSubject()).toString();
    }
}
