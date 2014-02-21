package edu.stanford.bmir.protege.web.client.ui.ontology.individuals;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.UrlParam;
import edu.stanford.bmir.protege.web.client.rpc.OntologyServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.PaginationData;
import edu.stanford.bmir.protege.web.client.ui.util.GWTProxy;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.List;

public class PagedIndividualsProxyImpl  extends GWTProxy {

    private ProjectId projectId;
    private String className;

    public void setProjectId(ProjectId projectId) {
        this.projectId = projectId;
    }


    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    @Override
    public void load(int start, int limit, String sort, String dir, final JavaScriptObject o, UrlParam[] baseParams) {

        OntologyServiceManager.getInstance().getIndividuals(projectId, className, start, limit, sort, dir,
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
