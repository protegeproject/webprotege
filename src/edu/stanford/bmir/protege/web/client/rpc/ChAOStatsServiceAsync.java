package edu.stanford.bmir.protege.web.client.rpc;

import java.util.Collection;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.stanford.bmir.protege.web.client.rpc.data.ChangeData;
import edu.stanford.bmir.protege.web.client.rpc.data.PaginationData;

/**
 * @author Sean Falconer <sean.falconer@stanford.edu>
 */
public interface ChAOStatsServiceAsync {
	void applyChangeFilter(String projectName, List<String> authors, AsyncCallback<Void> callback);

	void getChangeAuthors(String projectName, AsyncCallback<Collection<String>> callback);

	void getCompositeChanges(String projectName, String entityName,
			List<String> authors, int start, int limit, String sort,
			String dir, AsyncCallback<PaginationData<ChangeData>> callback);

	void getNumChanges(String projectName, String entityName, AsyncCallback<Integer> callback);

	void getNumChildrenChanges(String projectName, String entityName, AsyncCallback<Integer> callback);

}
