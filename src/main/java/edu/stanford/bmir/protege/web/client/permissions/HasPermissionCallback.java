package edu.stanford.bmir.protege.web.client.permissions;

import com.google.gwt.user.client.ui.HasEnabled;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/01/16
 */
public class HasPermissionCallback extends DispatchServiceCallback<Boolean> {

    private List<HasEnabled> permissionEnabledItems = new ArrayList<>();

    public HasPermissionCallback(HasEnabled ... permissionEnabledItems) {
        this.permissionEnabledItems.addAll(Arrays.asList(permissionEnabledItems));
    }

    @Override
    public void handleSuccess(Boolean hasPermission) {
        for(HasEnabled hasEnabled : permissionEnabledItems) {
            hasEnabled.setEnabled(hasPermission);
        }
    }
}
