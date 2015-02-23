package edu.stanford.bmir.protege.web.server;

import edu.stanford.bmir.protege.web.client.rpc.AdminService;
import edu.stanford.bmir.protege.web.server.metaproject.MetaProjectManager;
import edu.stanford.bmir.protege.web.shared.permissions.Permission;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionsSet;
import edu.stanford.smi.protege.server.metaproject.Operation;
import java.util.Collection;

/**
 * Administrative services for user management
 *
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 * @author Tania Tudorache <tudorache@stanford.edu>
 */
public class AdminServiceImpl extends WebProtegeRemoteServiceServlet implements AdminService {

    private static final long serialVersionUID = 7616699639338297327L;

    public PermissionsSet getAllowedOperations(String project, String user) {
        Collection<Operation> ops = MetaProjectManager.getManager().getAllowedOperations(project, user);
        return toPermissionSet(ops);
    }

    private PermissionsSet toPermissionSet(Collection<Operation> ops) {
        PermissionsSet.Builder builder = PermissionsSet.builder();
        for (Operation op : ops) {
            builder.addPermission(Permission.getPermission(op.getName()));
        }
        return builder.build();
    }

}
