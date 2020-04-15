package edu.stanford.bmir.protege.web.shared.project;

import com.google.common.base.MoreObjects;
import edu.stanford.bmir.protege.web.shared.access.ActionId;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-14
 */
public class GetAvailableProjectsWithPermissionAction implements Action<GetAvailableProjectsWithPermissionResult> {

    private ActionId permission;

    public GetAvailableProjectsWithPermissionAction(@Nonnull ActionId permission) {
        this.permission = checkNotNull(permission);
    }

    @GwtSerializationConstructor
    private GetAvailableProjectsWithPermissionAction() {
    }

    @Nonnull
    public ActionId getPermission() {
        return permission;
    }

    @Override
    public int hashCode() {
        return permission.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof GetAvailableProjectsWithPermissionAction)) {
            return false;
        }
        GetAvailableProjectsWithPermissionAction other = (GetAvailableProjectsWithPermissionAction) obj;
        return this.permission.equals(other.permission);
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper("GetAvailableProjectsWithPermissionAction")
                          .addValue(permission)
                          .toString();
    }
}
