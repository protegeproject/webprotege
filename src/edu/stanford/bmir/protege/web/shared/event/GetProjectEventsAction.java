package edu.stanford.bmir.protege.web.shared.event;

import com.google.common.base.Optional;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.InvocationException;
import edu.stanford.bmir.protege.web.shared.dispatch.InvocationExceptionTolerantAction;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.shared.dispatch.HasProjectAction;
import edu.stanford.bmir.protege.web.shared.events.EventTag;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/03/2013
 */
public class GetProjectEventsAction implements HasProjectAction<GetProjectEventsResult>, InvocationExceptionTolerantAction {

    private ProjectId projectId;

    private UserId userId;

    private EventTag sinceTag;

    /**
     * For serialization purposes only.
     */
    private GetProjectEventsAction() {
    }

    public GetProjectEventsAction(EventTag sinceTag, ProjectId projectId, UserId userId) {
        this.sinceTag = sinceTag;
        this.projectId = projectId;
        this.userId = userId;
    }

    public EventTag getSinceTag() {
        return sinceTag;
    }

    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    public UserId getUserId() {
        return userId;
    }

    @Override
    public Optional<String> handleInvocationException(InvocationException ex) {
        GWT.log("Could not retrieve events due to server connection problems.");
        return Optional.absent();
    }
}
