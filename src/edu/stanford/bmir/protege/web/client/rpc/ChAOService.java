package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import edu.stanford.bmir.protege.web.client.rpc.data.*;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * A service for ontology changes and annotations.
 * <p />
 * @author Csongor Nyulas <csongor.nyulas@stanford.edu>
 * @author Tania Tudorache <tudorache@stanford.edu>
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 */
@RemoteServiceRelativePath("chao")
public interface ChAOService extends RemoteService {

    /*
     * Changes
     */
    public ChangeData createChange(String projectName, String userName, String entityName, final String action, final String context);

    public PaginationData<ChangeData> getChanges(String projectName, Date startDate, Date endDate, int start, int limit, String sort, String dir);

    public PaginationData<ChangeData> getChanges(String projectName, String entityName, int start, int limit, String sort, String dir);

    public Collection<ChangeData> getChanges(String projectName, Date start, Date end);

    public Integer getNumChanges(String projectName, Date start, Date end);

    public Collection<ChangeData> getChanges(String projectName, String entityName);

    /*
     * Watched entities
     */

    public PaginationData<ChangeData> getWatchedEntities(String projectName, String userName, int start, int limit, String sort, String dir);

    public Collection<ChangeData> getWatchedEntities(String projectName, String userName);


}
