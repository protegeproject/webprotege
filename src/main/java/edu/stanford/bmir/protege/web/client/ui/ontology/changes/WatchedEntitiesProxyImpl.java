package edu.stanford.bmir.protege.web.client.ui.ontology.changes;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.UrlParam;
import edu.stanford.bmir.protege.web.client.rpc.ChAOServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.ChangeData;
import edu.stanford.bmir.protege.web.client.rpc.data.PaginationData;
import edu.stanford.bmir.protege.web.client.ui.util.GWTProxy;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import java.util.ArrayList;

class WatchedEntitiesProxyImpl extends GWTProxy {

    private ProjectId projectId;

    private UserId userName;
    
    public ProjectId getProjectId() {
        return projectId;
    }

    public void setProjectId(ProjectId projectName) {
        this.projectId = projectName;
    }

    public UserId getUserName() {
        return userName;
    }
    
    public void resetParams(){
        this.projectId = null;
        this.userName = null;
    }

    public void setUserName(UserId userName) {
        this.userName = userName;
    }

    public WatchedEntitiesProxyImpl() {

    }

    @Override
    public void load(int start, int limit, String sort, String dir, final JavaScriptObject o, UrlParam[] baseParams) {

        ChAOServiceManager.getInstance().getWatchedEntities(projectId, userName, start, limit, sort, dir,
                new AsyncCallback<PaginationData<ChangeData>>() {
                    public void onFailure(Throwable caught) {
                        loadResponse(o, false, 0, (JavaScriptObject) null);
                    }

                    public void onSuccess(PaginationData<ChangeData> result) {
                        if (result != null && result.getTotalRecords() != 0) {
                            loadResponse(o, true, result.getTotalRecords(), getRows(result));
                        } else {
                            loadResponse(o, false, 0, (JavaScriptObject) null);
                        }
                    }

                    private Object[][] getRows(PaginationData<ChangeData> result) {

                        ArrayList<ChangeData> data = result.getData();
                        Object[][] resultAsObjects = new Object[data.size()][5];

                        int i = 0;
                        for (ChangeData record : data) {
                            Object[] obj = getRow(record.getAuthor(), record.getDescription(), "", (record
                                    .getTimestamp() == null) ? "" : record.getTimestamp(), record.getEntityData()
                                    .getBrowserText());
                            resultAsObjects[i++] = obj;
                        }

                        return resultAsObjects;
                    }

                    private Object[] getRow(Object author, Object desc, Object appliesTo, Object timestamp,
                            Object browserText) {
                        return new Object[] { author, desc, appliesTo, timestamp, browserText };
                    }
                });
    }

}