package edu.stanford.bmir.protege.web.shared.frame;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25/07/15
 */
public class GetOntologyFramesAction extends AbstractHasProjectAction<GetOntologyFramesResult> {

    private GetOntologyFramesAction() {
    }

    public GetOntologyFramesAction(ProjectId projectId) {
        super(projectId);
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper("GetOntologyFramesAction")
                          .addValue(getProjectId())
                          .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getProjectId());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetOntologyFramesAction)) {
            return false;
        }
        GetOntologyFramesAction other = (GetOntologyFramesAction) obj;
        return this.getProjectId().equals(other.getProjectId());
    }
}
