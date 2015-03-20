package edu.stanford.bmir.protege.web.shared.change;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.event.HasEventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/03/15
 */
public class RevertRevisionResult implements Result, HasProjectId, HasEventList<ProjectEvent<?>> {

    private ProjectId projectId;

    private EventList<ProjectEvent<?>> eventList;

    private RevertRevisionResult() {
    }

    public RevertRevisionResult(ProjectId projectId, EventList<ProjectEvent<?>> eventList) {
        this.projectId = checkNotNull(projectId);
        this.eventList = checkNotNull(eventList);
    }

    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Override
    public EventList<ProjectEvent<?>> getEventList() {
        return eventList;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId, eventList.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof RevertRevisionResult)) {
            return false;
        }
        RevertRevisionResult other = (RevertRevisionResult) obj;
        return this.projectId.equals(other.projectId) && this.eventList.equals(other.eventList);
    }


    @Override
    public String toString() {
        return toStringHelper("RevertRevisionResult")
                .addValue(projectId)
                .toString();
    }
}
