package edu.stanford.bmir.protege.web.client.ui.portlet.propertyForm;

import com.gwtext.client.widgets.MessageBox;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.client.ui.util.UIUtil;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/12/15
 */
public class ReadWriteOperationChecker {

    private LoggedInUserProvider loggedInUserProvider;

    private LoggedInUserProjectPermissionChecker permissionChecker;

    public boolean isWriteOperationAllowed() {
        return isWriteOperationAllowed(true);
    }

    public boolean isWriteOperationAllowed(boolean showUserAlerts) {
        if (!checkOperationAllowed(showUserAlerts)) {
            return false;
        }
        if (isReadOnly()) {
            if (showUserAlerts) {
                MessageBox.alert("This property is read only.");
            }
            return false;
        }
         boolean hasWriteAccess = userPartOfWriteAccessGroup();
         if (!hasWriteAccess) {
             if (showUserAlerts) {
                 MessageBox.alert("You do not have permission to change this property.");
             }
         }
         return hasWriteAccess;
    }

    private boolean checkOperationAllowed(boolean showUserAlerts) {
        if (loggedInUserProvider.getCurrentUserId().isGuest()) {
            if (showUserAlerts) {
                MessageBox.alert("Sign in", "Please sign in first.");
            }
            return false;
        }
        if (permissionChecker.hasWritePermission()) {
            return true;
        }
        else {
            if (showUserAlerts) {
                MessageBox.alert("No permission", "You do not have write permission.");
            }
            return false;
        }
    }

}
