package edu.stanford.bmir.protege.web.shared.obo;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.client.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Jun 2017
 */
public class GetOboTermDefinitionAction extends AbstractHasProjectAction<GetOboTermDefinitionResult> {

    private OWLEntity term;

    @GwtSerializationConstructor
    private GetOboTermDefinitionAction() {
    }

    public GetOboTermDefinitionAction(ProjectId projectId, OWLEntity term) {
        super(projectId);
        this.term = term;
    }

    public OWLEntity getTerm() {
        return term;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(term, getProjectId());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetOboTermDefinitionAction)) {
            return false;
        }
        GetOboTermDefinitionAction other = (GetOboTermDefinitionAction) obj;
        return this.term.equals(other.term)
                && this.getProjectId().equals(other.getProjectId());
    }


    @Override
    public String toString() {
        return toStringHelper("GetOboTermDefinitionAction")
                .addValue(getProjectId())
                .addValue(term)
                .toString();
    }
}
