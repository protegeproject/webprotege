package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.rpc.data.*;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author Csongor Nyulas <csongor.nyulas@stanford.edu>
 * @author Tania Tudorache <tudorache@stanford.edu>
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 */
public interface ChAOServiceAsync {

    /*
     * Change methods
     */

    void createChange(String projectName, String userName, String entityName, String action, String context, AsyncCallback<ChangeData> async);

    void getChanges(String projectName, Date startDate, Date endDate, int start, int limit, String sort, String dir, AsyncCallback <PaginationData<ChangeData>> cb);

    void getChanges(String projectName, String entityName, int start, int limit, String sort, String dir, AsyncCallback<PaginationData<ChangeData>> cb);

    void getChanges(String projectName, Date start, Date end, AsyncCallback<Collection<ChangeData>> cb);

    void getNumChanges(String projectName, Date start, Date end, AsyncCallback<Integer> cb);

    void getChanges(String projectName, String entityName, AsyncCallback<Collection<ChangeData>> cb);

    /*
     * Watched entities
     */

	void getWatchedEntities(String projectName, String userName, int start, int limit, String sort, String dir, AsyncCallback<PaginationData<ChangeData>> cb);

    void getWatchedEntities(String projectName, String userName, AsyncCallback<Collection<ChangeData>> cb);


}
