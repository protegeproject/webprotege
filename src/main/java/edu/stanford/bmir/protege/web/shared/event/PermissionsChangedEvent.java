package edu.stanford.bmir.protege.web.shared.event;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 03/04/2013
 */
public class PermissionsChangedEvent extends ProjectEvent<PermissionsChangedHandler> {

    public static final transient Type<PermissionsChangedHandler> TYPE = new Type<PermissionsChangedHandler>();


    public PermissionsChangedEvent(ProjectId source) {
        super(source);
    }

    /**
     * For serialization only
     */
    private PermissionsChangedEvent() {
    }

    @Override
    public Type<PermissionsChangedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(PermissionsChangedHandler handler) {
        handler.handlePersmissionsChanged(this);
    }

    @Override
    public int hashCode() {
        return "PermissionsChangedEvent".hashCode() + getProjectId().hashCode();
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
        StringBuilder sb = new StringBuilder();
        sb.append("PermissionsChangedEvent");
        sb.append("(");
        sb.append(getProjectId());
        sb.append(")");
        return sb.toString();
    }
}
