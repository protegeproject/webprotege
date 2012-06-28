package edu.stanford.bmir.protege.web.client.rpc;

import java.util.Collection;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityPropertyValues;
import edu.stanford.bmir.protege.web.client.rpc.data.SubclassEntityData;

public interface ICDServiceAsync {

    void createICDCls(String projectName, String clsName, Collection<String> superClsNames, String title, String sortingLabel,
            boolean createICDSpecificEntities, String user, String operationDescription, String reasonForChange, AsyncCallback<EntityData> cb);

    void getEntityPropertyValuesForLinearization(String projectName, List<String> entities, List<String> properties,
            List<String> reifiedProps, AsyncCallback<List<EntityPropertyValues>> cb);

    void exportICDBranch(String projectName, String topNode, String userName, AsyncCallback<String> cb);

    void getSecondaryAndInheritedTags(String projectName, String clsName, AsyncCallback<List<EntityPropertyValues>> cb);

    void getSubclasses(String projectName, String className, AsyncCallback<List<SubclassEntityData>> cb);

    void changeIndexType(String projectName, String subject, String indexEntity, List<String> reifiedProps, 
            String indexType, AsyncCallback<EntityPropertyValues> cb);

    void changeInclusionFlagForIndex(String projectName, String subject, String indexEntity, List<String> reifiedProps,
            boolean isInclusionFlag, AsyncCallback<EntityPropertyValues> callback);

}
