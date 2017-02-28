package edu.stanford.bmir.protege.web.shared.permissions;

import com.google.common.base.Objects;
import com.google.web.bindery.event.shared.Event;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 03/04/2013
 *
 * An event that is fired when the permissions for a project change.
 */
public class PermissionsChangedEvent extends ProjectEvent<PermissionsChangedHandler> {

    public static final transient Event.Type<PermissionsChangedHandler> ON_PERMISSIONS_CHANGED = new Event.Type<PermissionsChangedHandler>();


    public PermissionsChangedEvent(@Nonnull ProjectId source) {
        super(checkNotNull(source));
    }

    @GwtSerializationConstructor
    private PermissionsChangedEvent() {
    }

    @Override
    public Event.Type<PermissionsChangedHandler> getAssociatedType() {
        return ON_PERMISSIONS_CHANGED;
    }

    @Override
    protected void dispatch(PermissionsChangedHandler handler) {
        handler.handlePersmissionsChanged(this);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getProjectId());
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof PermissionsChangedEvent)) {
            return false;
        }
        PermissionsChangedEvent other = (PermissionsChangedEvent) obj;
        return this.getProjectId().equals(other.getProjectId());
    }


    @Override
    public String toString() {
        return toStringHelper("PermissionsChangedEvent")
                .addValue(getProjectId())
                .toString();
    }
}
