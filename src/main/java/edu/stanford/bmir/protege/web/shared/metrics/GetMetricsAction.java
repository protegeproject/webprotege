package edu.stanford.bmir.protege.web.shared.metrics;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.client.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 26/04/2014
 */
public class GetMetricsAction extends AbstractHasProjectAction<GetMetricsResult> {

    private GetMetricsAction() {
    }

    public GetMetricsAction(ProjectId projectId) {
        super(projectId);
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof GetMetricsAction)) {
            return false;
        }
        GetMetricsAction other = (GetMetricsAction) o;
        return this.getProjectId().equals(other.getProjectId());
    }

    @Override
    public int hashCode() {
        return "GetMetricsAction".hashCode() + getProjectId().hashCode();
    }

    @Override
    public String toString() {
        return Objects.toStringHelper("GetMetricsAction")
                .addValue(getProjectId()).toString();
    }

}
