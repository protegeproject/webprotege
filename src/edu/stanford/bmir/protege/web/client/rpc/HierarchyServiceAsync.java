package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;

import java.util.Collection;
import java.util.List;

public interface HierarchyServiceAsync {

    void changeParent(String project, String className, Collection<String> parentsToAdd,
            Collection<String> parentsToRemove, String user, String operationDescription, String reasonForChange,
            AsyncCallback<List<EntityData>> callback);

    void retireClasses(String project, Collection<String> classesToRetireNames, boolean retireChildren,
            String newParent, String reasonForChange, String operationDescription, String user,
            AsyncCallback<Void> callback);

}
