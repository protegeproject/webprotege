package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import java.util.Collection;
import java.util.List;

public class HierarchyServiceManager {

    static HierarchyServiceManager instance;
    private static HierarchyServiceAsync proxy;

    private HierarchyServiceManager() {
        proxy = (HierarchyServiceAsync) GWT.create(HierarchyService.class);
    }

    public static HierarchyServiceManager getInstance() {
        if (instance == null) {
            instance = new HierarchyServiceManager();
        }
        return instance;
    }


    public void changeParent(ProjectId project, String className, Collection<String> parentsToAdd,
            Collection<String> parentsToRemove, UserId user, String operationDescription, String reasonForChange,
            AsyncCallback<List<EntityData>> callback) {
        proxy.changeParent(project.getId(), className, parentsToAdd, parentsToRemove, user.getUserName(), operationDescription, reasonForChange, callback);
    }

    public void retireClasses(ProjectId project, Collection<String> classesToRetireNames, boolean retireChildren,
            String newParent, String reasonForChange, String operationDescription, UserId user,
            AsyncCallback<Void> callback) {
        proxy.retireClasses(project.getId(), classesToRetireNames, retireChildren, newParent,
                reasonForChange, operationDescription, user.getUserName(), callback);
    }
}
