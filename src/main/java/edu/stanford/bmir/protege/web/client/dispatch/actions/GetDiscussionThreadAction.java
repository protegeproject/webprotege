package edu.stanford.bmir.protege.web.client.dispatch.actions;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.client.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/04/2013
 */
public class GetDiscussionThreadAction extends AbstractHasProjectAction<GetDiscussionThreadResult> {

    private OWLEntity targetEntity;


    /**
     * For serialization purposes only
     */
    @SuppressWarnings("unused")
    private GetDiscussionThreadAction() {
    }

    public GetDiscussionThreadAction(ProjectId projectId, OWLEntity targetEntity) {
        super(projectId);
        this.targetEntity = checkNotNull(targetEntity);
    }

    public OWLEntity getTargetEntity() {
        return targetEntity;
    }

    @Override
    public int hashCode() {
        return "GetDiscussionThreadAction".hashCode() + targetEntity.hashCode() + getProjectId().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof GetDiscussionThreadAction)) {
            return false;
        }
        GetDiscussionThreadAction other = (GetDiscussionThreadAction) o;
        return this.targetEntity.equals(other.targetEntity) && this.getProjectId().equals(other.getProjectId());
    }

    @Override
    public String toString() {
        return Objects.toStringHelper("GetDiscussionThreadAction")
                .addValue(getProjectId())
                .add("entity", targetEntity).toString();
    }
}
