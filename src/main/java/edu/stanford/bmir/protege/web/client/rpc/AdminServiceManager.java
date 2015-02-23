package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionsSet;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

/**
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 * @author Tania Tudorache <tudorache@stanford.edu>
 */
public class AdminServiceManager {

    private static AdminServiceAsync proxy;
    static AdminServiceManager instance;

    private AdminServiceManager() {
        proxy = GWT.create(AdminService.class);
    }

    public static AdminServiceManager getInstance() {
        if (instance == null) {
            instance = new AdminServiceManager();
        }
        return instance;
    }

    public void getAllowedOperations(ProjectId projectId, UserId userId, AsyncCallback<PermissionsSet> cb) {
        proxy.getAllowedOperations(projectId.getId(), userId.getUserName(), cb);
    }
}
