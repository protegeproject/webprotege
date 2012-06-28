package edu.stanford.bmir.protege.web.client.ui.ontology.changeanalysis;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.UrlParam;

import edu.stanford.bmir.protege.web.client.rpc.ChAOStatsServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.ChangeData;
import edu.stanford.bmir.protege.web.client.rpc.data.PaginationData;
import edu.stanford.bmir.protege.web.client.ui.ontology.changes.ChangesProxyImpl;

/**
 * @author Sean Falconer <sean.falconer@stanford.edu>
 */
public class FilteringChangesProxyImpl extends ChangesProxyImpl {
	private List<String> authors;
	
	@Override
    public void load(int start, int limit, String sort, String dir, final JavaScriptObject o, UrlParam[] baseParams) {

        class ChangesHandler implements AsyncCallback<PaginationData<ChangeData>> {
            public void onFailure(Throwable caught) {
                loadResponse(o, false, 0, (JavaScriptObject) null);
            }

            public void onSuccess(PaginationData<ChangeData> result) {
                if (result != null && result.getTotalRecords() != 0) {
                    loadResponse(o, true, result.getTotalRecords(), getRecords(result));
                } else {
                    loadResponse(o, false, 0, (JavaScriptObject) null);
                }
            }

            private Object[][] getRecords(PaginationData<ChangeData> result) {
                ArrayList<ChangeData> data = result.getData();
                Object[][] resultAsObjects = new Object[data.size()][4];

                int i = 0;
                for (ChangeData record : data) {
                    Object[] obj = getRow(record.getDescription(), record.getAuthor(), record.getTimestamp(), "");
                    resultAsObjects[i++] = obj;
                }

                return resultAsObjects;
            }

            private Object[] getRow(Object desc, Object author, Object timestamp, Object appliesTo) {
                return new Object[] { desc, author, timestamp, appliesTo };
            }
        }

        if (getEntityName() != null) {
    		ChAOStatsServiceManager.getInstance().getCompositeChanges(getProjectName(), getEntityName(), authors, start, limit, sort, dir,
                    new ChangesHandler());
        } 
    }

	public void setAuthors(List<String> authors) {
		this.authors = new ArrayList<String>(authors);
	}

	public List<String> getAuthors() {
		return this.authors;
	}
}
