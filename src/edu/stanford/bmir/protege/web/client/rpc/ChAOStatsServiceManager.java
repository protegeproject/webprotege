package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.rpc.data.ChangeData;
import edu.stanford.bmir.protege.web.client.rpc.data.PaginationData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.Collection;
import java.util.List;

/**
 * @author Sean Falconer <sean.falconer@stanford.edu>
 */
public class ChAOStatsServiceManager {
	private static ChAOStatsServiceAsync proxy;
    static ChAOStatsServiceManager instance;
    
    private ChAOStatsServiceManager() {
    	proxy = (ChAOStatsServiceAsync) GWT.create(ChAOStatsService.class);
    }
    
    public static ChAOStatsServiceManager getInstance() {
        if (instance == null) {
            instance = new ChAOStatsServiceManager();
        }
        return instance;
    }
    
    public void applyChangeFilter(ProjectId projectId, List<String> authors, AsyncCallback<Void> callback) {
    	proxy.applyChangeFilter(projectId.getId(), authors, callback);
    }

	public void getChangeAuthors(ProjectId projectId, AsyncCallback<Collection<String>> callback) {
		proxy.getChangeAuthors(projectId.getId(), callback);
	}

	public void getCompositeChanges(ProjectId projectId, String entityName,
			List<String> authors, int start, int limit, String sort,
			String dir, AsyncCallback<PaginationData<ChangeData>> callback) {
		proxy.getCompositeChanges(projectId.getId(), entityName, authors, start, limit, sort, dir, callback);
	}

	public void getNumChanges(ProjectId projectId, String entityName, AsyncCallback<Integer> callback) {
		proxy.getNumChanges(projectId.getId(), entityName, callback);
	}

	public void getNumChildrenChanges(ProjectId projectId, String entityName, AsyncCallback<Integer> callback) {
		proxy.getNumChildrenChanges(projectId.getId(), entityName, callback);
	}
}
