package edu.stanford.bmir.protege.web.client.ui.ontology.individuals;

import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.UrlParam;

import edu.stanford.bmir.protege.web.client.rpc.OntologyServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.PaginationData;
import edu.stanford.bmir.protege.web.client.ui.util.GWTProxy;

public class PagedIndividualsProxyImpl  extends GWTProxy {

    private String projectName;
    private String className;

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    @Override
    public void load(int start, int limit, String sort, String dir, final JavaScriptObject o, UrlParam[] baseParams) {

        OntologyServiceManager.getInstance().getIndividuals(projectName, className, start, limit, sort, dir,
                new AsyncCallback<PaginationData<EntityData>>() {

                    public void onFailure(Throwable caught) {
                        loadResponse(o, false, 0, (JavaScriptObject) null);
                    }

                    public void onSuccess(PaginationData<EntityData> individuals) {
                        if (individuals != null && individuals.getTotalRecords() != 0) {
                            loadResponse(o, true, individuals.getTotalRecords(), getRows(individuals));
                        } else {
                            loadResponse(o, false, 0, (JavaScriptObject) null);
                        }
                    }

                    private Object[][] getRows(PaginationData<EntityData> individuals) {
                        List<EntityData> data = individuals.getData();
                        Object[][] resultAsObjects = new Object[data.size()][2];
                        int i = 0;
                        for (EntityData inst : data) {
                            resultAsObjects[i++] =new Object[]{inst.getName(), inst.getBrowserText()};
                        }
                        return resultAsObjects;
                    }
        });

    }

}
