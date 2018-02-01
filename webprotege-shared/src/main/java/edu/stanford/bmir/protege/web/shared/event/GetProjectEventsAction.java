package edu.stanford.bmir.protege.web.shared.event;

import com.google.common.base.Objects;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.InvocationException;
import edu.stanford.bmir.protege.web.shared.project.HasProjectId;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.InvocationExceptionTolerantAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import java.util.Optional;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/03/2013
 */
public class GetProjectEventsAction implements Action<GetProjectEventsResult>, InvocationExceptionTolerantAction, HasProjectId {

    private ProjectId projectId;

    private EventTag sinceTag;

    /**
     * For serialization purposes only.
     */
    private GetProjectEventsAction() {
    }

    public GetProjectEventsAction(@Nonnull EventTag sinceTag,
                                  @Nonnull ProjectId projectId) {
        this.sinceTag = checkNotNull(sinceTag);
        this.projectId = checkNotNull(projectId);
    }

    public EventTag getSinceTag() {
        return sinceTag;
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Override
    public Optional<String> handleInvocationException(InvocationException ex) {
        GWT.log("Could not retrieve events due to server connection problems.");
        return Optional.empty();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(sinceTag, projectId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetProjectEventsAction)) {
            return false;
        }
        GetProjectEventsAction other = (GetProjectEventsAction) obj;
        return this.sinceTag.equals(other.sinceTag)
                && this.projectId.equals(other.projectId);
    }

    @Override
    public String toString() {
        return toStringHelper("GetProjectEventsAction")
                          .addValue(projectId)
                          .add("since", sinceTag).toString();
    }
}
