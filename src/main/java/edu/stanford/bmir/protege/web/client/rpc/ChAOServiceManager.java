package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.rpc.data.ChangeData;
import edu.stanford.bmir.protege.web.client.rpc.data.PaginationData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import java.util.Collection;
import java.util.Date;

/**
 * @author Csongor Nyulas <csongor.nyulas@stanford.edu>
 * @author Tania Tudorache <tudorache@stanford.edu>
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 */
public class ChAOServiceManager {

    private static ChAOServiceAsync proxy;
    static ChAOServiceManager instance;

    private ChAOServiceManager() {
        proxy = GWT.create(ChAOService.class);
    }

    public static ChAOServiceManager getInstance() {
        if (instance == null) {
            instance = new ChAOServiceManager();
        }
        return instance;
    }

    public void getChanges(ProjectId projectId, Date startDate, Date endDate, int start, int limit, String sort,
            String dir, AsyncCallback<PaginationData<ChangeData>> cb) {
        proxy.getChanges(projectId.getId(), startDate, endDate, start, limit, sort, dir, cb);
    }

    public void getChanges(ProjectId projectId, String entityName, int start, int limit, String sort, String dir,
            AsyncCallback<PaginationData<ChangeData>> cb) {
        proxy.getChanges(projectId.getId(), entityName, start, limit, sort, dir, cb);
    }

    public void getNumChanges(ProjectId projectId, Date start, Date end, AsyncCallback<Integer> cb) {
        proxy.getNumChanges(projectId.getId(), start, end, cb);
    }
}
