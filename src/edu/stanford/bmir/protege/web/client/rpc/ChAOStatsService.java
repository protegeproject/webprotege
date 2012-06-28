package edu.stanford.bmir.protege.web.client.rpc;

import java.util.Collection;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import edu.stanford.bmir.protege.web.client.rpc.data.ChangeData;
import edu.stanford.bmir.protege.web.client.rpc.data.PaginationData;

/**
 * A service for statistics related to ontology changes.
 *
 * @author Sean Falconer <sean.falconer@stanford.edu>
 */
@RemoteServiceRelativePath("chaostats")
public interface ChAOStatsService extends RemoteService {
	 public void applyChangeFilter(String projectName, List<String> authors);
	 
	 public Collection<String> getChangeAuthors(String projectName);
	 
	 public Integer getNumChanges(String projectName, String entityName);
	 
	 public Integer getNumChildrenChanges(String projectName, String entityName);
	 
	 public PaginationData<ChangeData> getCompositeChanges(String projectName, String entityName, List<String> authors, int start, int limit, String sort,
	            String dir);
}
