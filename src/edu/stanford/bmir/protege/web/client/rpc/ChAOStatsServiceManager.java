package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.rpc.data.ChangeData;
import edu.stanford.bmir.protege.web.client.rpc.data.PaginationData;

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
    
    public void applyChangeFilter(String projectName, List<String> authors, AsyncCallback<Void> callback) {
    	proxy.applyChangeFilter(projectName, authors, callback);
    }

	public void getChangeAuthors(String projectName, AsyncCallback<Collection<String>> callback) {
		proxy.getChangeAuthors(projectName, callback);
	}

	public void getCompositeChanges(String projectName, String entityName,
			List<String> authors, int start, int limit, String sort,
			String dir, AsyncCallback<PaginationData<ChangeData>> callback) {
		proxy.getCompositeChanges(projectName, entityName, authors, start, limit, sort, dir, callback);
	}

	public void getNumChanges(String projectName, String entityName, AsyncCallback<Integer> callback) {
		proxy.getNumChanges(projectName, entityName, callback);
	}

	public void getNumChildrenChanges(String projectName, String entityName, AsyncCallback<Integer> callback) {
		proxy.getNumChildrenChanges(projectName, entityName, callback);
	}
}
