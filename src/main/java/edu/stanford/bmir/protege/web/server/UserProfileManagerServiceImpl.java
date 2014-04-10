package edu.stanford.bmir.protege.web.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import edu.stanford.bmir.protege.web.client.rpc.UserProfileManagerService;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.smi.protege.server.metaproject.MetaProject;
import edu.stanford.smi.protege.server.metaproject.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/02/2012
 */
public class UserProfileManagerServiceImpl extends WebProtegeRemoteServiceServlet implements UserProfileManagerService {

    public List<UserId> getUserIds() {
        MetaProjectManager mpm = MetaProjectManager.getManager();
        MetaProject mp = mpm.getMetaProject();
        List<UserId> result = new ArrayList<UserId>();
        for(User user : mp.getUsers()) {
            result.add(UserId.getUserId(user.getName()));
        }
        return result;
    }
}
