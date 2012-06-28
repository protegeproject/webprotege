package edu.stanford.bmir.protege.web.client.rpc;

import java.util.Collection;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;

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


    public void changeParent(String project, String className, Collection<String> parentsToAdd,
            Collection<String> parentsToRemove, String user, String operationDescription, String reasonForChange,
            AsyncCallback<List<EntityData>> callback) {
        proxy.changeParent(project, className, parentsToAdd, parentsToRemove, user, operationDescription, reasonForChange, callback);
    }

    public void retireClasses(String project, Collection<String> classesToRetireNames, boolean retireChildren,
            String newParent, String reasonForChange, String operationDescription, String user,
            AsyncCallback<Void> callback) {
        proxy.retireClasses(project, classesToRetireNames, retireChildren, newParent,
                reasonForChange, operationDescription, user, callback);
    }
}
