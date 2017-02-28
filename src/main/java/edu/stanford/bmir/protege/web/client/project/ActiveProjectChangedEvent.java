package edu.stanford.bmir.protege.web.client.project;

import com.google.web.bindery.event.shared.Event;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.Optional;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 07/04/2013
 */
public class ActiveProjectChangedEvent extends WebProtegeEvent<ActiveProjectChangedHandler> {

    public static final transient Event.Type<ActiveProjectChangedHandler> TYPE = new Event.Type<ActiveProjectChangedHandler>();

    private ProjectId projectId;

    @GwtSerializationConstructor
    private ActiveProjectChangedEvent() {
    }

    /**
     * Constructs an {@link ActiveProjectChangedEvent}.
     * @param newProjectId The id of the new active project.  Not {@code null}.
     * @throws NullPointerException if {@code newProjectId} is {@code null}.
     */
    public ActiveProjectChangedEvent(Optional<ProjectId> newProjectId) {
        this.projectId = checkNotNull(newProjectId).orElse(null);
    }

    /**
     * Gets the project id of the new active project.
     * @return An optional {@link ProjectId} which, if present correponds to the id of the new active project.
     * Not {@code null}.  An absent value indicates that no project is active (because the user has switched to the
     * home tab for example).
     */
    public Optional<ProjectId> getProjectId() {
        return Optional.ofNullable(projectId);
    }

    @Override
    public Event.Type<ActiveProjectChangedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ActiveProjectChangedHandler handler) {
        handler.handleActiveProjectChanged(this);
    }


    @Override
    public String toString() {
        return toStringHelper("ActiveProjectChangedEvent")
                .addValue(projectId)
                .toString();
    }
}
