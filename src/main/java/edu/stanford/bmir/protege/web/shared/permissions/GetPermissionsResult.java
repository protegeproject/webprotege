package edu.stanford.bmir.protege.web.shared.permissions;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23/02/15
 */
public class GetPermissionsResult implements Result {

    private PermissionsSet permissionsSet;

    private GetPermissionsResult() {
    }

    public GetPermissionsResult(PermissionsSet permissionsSet) {
        this.permissionsSet = checkNotNull(permissionsSet);
    }

    public PermissionsSet getPermissionsSet() {
        return permissionsSet;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(permissionsSet);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetPermissionsResult)) {
            return false;
        }
        GetPermissionsResult other = (GetPermissionsResult) obj;
        return this.permissionsSet.equals(other.permissionsSet);
    }


    @Override
    public String toString() {
        return toStringHelper("GetPermissionsResult")
                .addValue(permissionsSet)
                .toString();
    }
}
