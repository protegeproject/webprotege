package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.rpc.data.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import java.util.List;

public class OntologyServiceManager {

    private static OntologyServiceAsync proxy;
    static OntologyServiceManager instance;

    public static OntologyServiceManager getInstance() {
        if (instance == null) {
            instance = new OntologyServiceManager();
        }
        return instance;
    }

    private OntologyServiceManager() {
        proxy = GWT.create(OntologyService.class);
    }

    public void getImportedOntologies(ProjectId projectId, AsyncCallback<ImportsData> cb) {
        proxy.getImportedOntologies(projectId.getId(), cb);
    }

    public void getRootEntity(ProjectId projectId, AsyncCallback<EntityData> cb) {
        proxy.getRootEntity(projectId.getId(), cb);
    }

    /*
     * Class methods
     */

    public void getSubclasses(ProjectId projectId, String className, AsyncCallback<List<SubclassEntityData>> cb) {
        proxy.getSubclasses(projectId.getId(), className, cb);
    }

    public void moveCls(ProjectId projectId, String clsName, String oldParentName, String newParentName, boolean checkForCycles,
            UserId userId, String operationDescription, AsyncCallback<List<EntityData>> cb) {
        proxy.moveCls(projectId.getId(), clsName, oldParentName, newParentName, checkForCycles, userId.getUserName(), operationDescription, cb);
    }

    public void getSubproperties(ProjectId projectId, String propertyName, AsyncCallback<List<EntityData>> cb) {
        proxy.getSubproperties(projectId.getId(), propertyName, cb);
    }

    /*
     * Search
     */

    public void search(ProjectId projectId, String searchString, AsyncCallback<List<EntityData>> cb) {
        proxy.search(projectId.getId(), searchString, cb);
    }

    public void search(ProjectId projectId, String searchString, ValueType valueType, AsyncCallback<List<EntityData>> cb) {
        proxy.search(projectId.getId(), searchString, valueType, cb);
    }

    public void search(ProjectId projectId, String searchString, ValueType valueType, int start, int limit, String sort, String dir, AsyncCallback<PaginationData<EntityData>> cb) {
        proxy.search(projectId.getId(), searchString, valueType, start, limit, sort, dir, cb);
    }

    public void getPathToRoot(ProjectId projectId, String entityName, AsyncCallback<List<EntityData>> cb) {
        proxy.getPathToRoot(projectId.getId(), entityName, cb);
    }
}
