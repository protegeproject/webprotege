package edu.stanford.bmir.protege.web.client.rpc;

import java.util.Collection;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;

/**
 * This interface has methods for managing the class hierarchy,
 * such as adding, removing and changing parents, retiring classes, etc.
 *
 * @author Tania Tudorache <tudorache@stanford.edu>
 *
 */
@RemoteServiceRelativePath("hierarchy")
public interface HierarchyService extends RemoteService{

    List<EntityData> changeParent(String project, String className, Collection<String> parentsToAdd, Collection<String> parentsToRemove,
            String user, String operationDescription, String reasonForChange);

    void retireClasses(String project, Collection<String> classesToRetireNames, boolean retireChildren, String newParent,
            String reasonForChange, String operationDescription, String user);

}
