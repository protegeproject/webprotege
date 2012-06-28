package edu.stanford.bmir.protege.web.client.ui.ontology.changes;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.UrlParam;

import edu.stanford.bmir.protege.web.client.rpc.ChAOServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.ChangeData;
import edu.stanford.bmir.protege.web.client.rpc.data.PaginationData;
import edu.stanford.bmir.protege.web.client.ui.util.GWTProxy;

public class ChangesProxyImpl extends GWTProxy {

    private String projectName = null;
    private String entityName = null;
    private Date startDate = null;
    private Date endDate = null;
    
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void resetParams(){
        this.projectName = null;
        this.entityName = null;
        this.startDate = null;
        this.endDate = null;
    }
    
    
    public ChangesProxyImpl() { 
    }
    
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

        if (entityName != null) {
            ChAOServiceManager.getInstance().getChanges(projectName, entityName, start, limit, sort, dir,
                    new ChangesHandler());
        } else { //no entity, get changes for the entire project
            ChAOServiceManager.getInstance().getChanges(projectName, startDate, endDate, start, limit, sort, dir,
                    new ChangesHandler());
        }
    }
}