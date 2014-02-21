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
        proxy = (OntologyServiceAsync) GWT.create(OntologyService.class);
    }

    public void hasWritePermission(ProjectId projectId, UserId userId, AsyncCallback<Boolean> cb) {
        proxy.hasWritePermission(projectId.getId(), userId.getUserName(), cb);
    }

    public void getImportedOntologies(ProjectId projectId, AsyncCallback<ImportsData> cb) {
        proxy.getImportedOntologies(projectId.getId(), cb);
    }

    public void getMetrics(ProjectId projectId, AsyncCallback<List<MetricData>> cb) {
        proxy.getMetrics(projectId.getId(), cb);
    }

    /*
     * Entity methods
     */

    public void getEntityTriples(ProjectId projectId, String entityName, AsyncCallback<List<Triple>> cb) {
        proxy.getEntityTriples(projectId.getId(), entityName, cb);
    }

    public void getEntityTriples(ProjectId projectId, List<String> entities, List<String> properties,
            AsyncCallback<List<Triple>> cb) {
        proxy.getEntityTriples(projectId.getId(), entities, properties, cb);
    }

    public void getEntityTriples(ProjectId projectId, List<String> entities, List<String> properties, List<String> reifiedProperties,
            AsyncCallback<List<Triple>> cb) {
        proxy.getEntityTriples(projectId.getId(), entities, properties, reifiedProperties, cb);
    }

    public void getEntityPropertyValues(ProjectId projectId, List<String> entities, List<String> properties, List<String> reifiedProps,
            AsyncCallback<List<EntityPropertyValues>> cb) {
        proxy.getEntityPropertyValues(projectId.getId(), entities, properties, reifiedProps, cb);
    }

    public void getRootEntity(ProjectId projectId, AsyncCallback<EntityData> cb) {
        proxy.getRootEntity(projectId.getId(), cb);
    }

    public void renameEntity(ProjectId projectId, String oldName, String newName, UserId userId,
            String operationDescription, AsyncCallback<EntityData> cb) {
        proxy.renameEntity(projectId.getId(), oldName, newName, userId.getUserName(), operationDescription, cb);
    }

    public void getEntity(ProjectId projectId, String entityName, AsyncCallback<EntityData> cb) {
        proxy.getEntity(projectId.getId(), entityName, cb);
    }

    /*
     * Class methods
     */

    public void getSubclasses(ProjectId projectId, String className, AsyncCallback<List<SubclassEntityData>> cb) {
        proxy.getSubclasses(projectId.getId(), className, cb);
    }

    public void getIndividuals(ProjectId projectId, String className, int start, int limit, String sort, String dir,
            AsyncCallback<PaginationData<EntityData>> cb) {
        proxy.getIndividuals(projectId.getId(), className, start, limit, sort, dir, cb);
    }

    public void moveCls(ProjectId projectId, String clsName, String oldParentName, String newParentName, boolean checkForCycles,
            UserId userId, String operationDescription, AsyncCallback<List<EntityData>> cb) {
        proxy.moveCls(projectId.getId(), clsName, oldParentName, newParentName, checkForCycles, userId.getUserName(), operationDescription, cb);
    }

    public void getClassConditions(ProjectId projectId, String className, AsyncCallback<List<ConditionItem>> cb) {
        proxy.getClassConditions(projectId.getId(), className, cb);
    }

    public void deleteCondition(ProjectId projectId, String className, ConditionItem conditionItem, int row, String operationDescription,
            AsyncCallback<List<ConditionItem>> cb) {
        proxy.deleteCondition(projectId.getId(), className, conditionItem, row, operationDescription, cb);
    }

    public  void replaceCondition(ProjectId projectId, String className, ConditionItem conditionItem, int row,
            String newCondition, String operationDescription, AsyncCallback<List<ConditionItem>> callback) {
        proxy.replaceCondition(projectId.getId(), className, conditionItem, row, newCondition, operationDescription, callback);
    }

    public void addCondition(ProjectId projectId, String className, int row, String newCondition, boolean isNS,
            String operationDescription, AsyncCallback<List<ConditionItem>> callback) {
        proxy.addCondition(projectId.getId(), className, row, newCondition, isNS, operationDescription, callback);
    }

    public void getConditionAutocompleteSuggestions(ProjectId projectId, String condition, int cursorPosition,
            AsyncCallback<ConditionSuggestion> callback) {
        proxy.getConditionAutocompleteSuggestions(projectId.getId(), condition, cursorPosition, callback);
    }

    public void getParents(ProjectId projectId, String className, boolean direct, AsyncCallback<List<EntityData>> cb) {
        proxy.getParents(projectId.getId(), className, direct, cb);
    }

    public void getRelatedProperties(ProjectId projectId, String className, AsyncCallback<List<Triple>> callback) {
        proxy.getRelatedProperties(projectId.getId(), className, callback);
    }


    public void getSubproperties(ProjectId projectId, String propertyName, AsyncCallback<List<EntityData>> cb) {
        proxy.getSubproperties(projectId.getId(), propertyName, cb);
    }

    public void addPropertyValue(ProjectId projectId, String entityName, PropertyEntityData propertyEntity,
            EntityData value, UserId userId, String operationDescription, AsyncCallback<Void> cb) {
        proxy.addPropertyValue(projectId.getId(), entityName, propertyEntity, value, userId.getUserName(), operationDescription, cb);
    }

    public void removePropertyValue(ProjectId projectId, String entityName, PropertyEntityData propertyEntity,
            EntityData value, UserId userId, String operationDescription, AsyncCallback<Void> cb) {
        proxy.removePropertyValue(projectId.getId(), entityName, propertyEntity, value, userId.getUserName(), operationDescription, cb);
    }

    public void replacePropertyValue(ProjectId projectId, String entityName, PropertyEntityData propertyEntity,
            EntityData oldValue, EntityData newValue, UserId userId, String operationDescription, AsyncCallback<Void> cb) {
        proxy.replacePropertyValue(projectId.getId(), entityName, propertyEntity, oldValue, newValue, userId.getUserName(),
                operationDescription, cb);
    }

    public void setPropertyValues(ProjectId projectId, String entityName,  PropertyEntityData propertyEntity,
            List<EntityData> values, UserId userId, String operationDescription, AsyncCallback<Void> cb) {
        proxy.setPropertyValues(projectId.getId(), entityName, propertyEntity, values, userId.getUserName(), operationDescription, cb);
    }

    /*
     * Instance methods
     */

    public void createInstance(ProjectId projectId, String instName, String typeName, UserId userId,
            String operationDescription, AsyncCallback<EntityData> cb) {
        proxy.createInstance(projectId.getId(), instName, typeName, userId.getUserName(), operationDescription, cb);
    }

    public void createInstanceValue(ProjectId projectId, String instName, String typeName, String subjectEntity,
            String propertyEntity, UserId userId, String operationDescription, AsyncCallback<EntityData> cb) {
        proxy.createInstanceValue(projectId.getId(), instName, typeName, subjectEntity, propertyEntity, userId.getUserName(),
                operationDescription, cb);
    }

    public void createInstanceValueWithPropertyValue(ProjectId projectId, String instName, String typeName,
            String subjectEntity, String propertyEntity, PropertyEntityData instancePropertyEntity,
            EntityData valueEntityData, UserId userId, String operationDescription, AsyncCallback<EntityData> cb) {
        proxy.createInstanceValueWithPropertyValue(projectId.getId(), instName, typeName, subjectEntity, propertyEntity,
                instancePropertyEntity, valueEntityData, userId.getUserName(), operationDescription, cb);
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

    public void getDirectTypes(ProjectId projectId, String instanceName, AsyncCallback<List<EntityData>> cb){
        proxy.getDirectTypes(projectId.getId(), instanceName, cb);
    }

    /*
     * Util methods
     */

    public void getBioPortalSearchContent(ProjectId projectId, String entityName, BioPortalSearchData bpSearchData,
            AsyncCallback<String> cb) {
        proxy.getBioPortalSearchContent(projectId.getId(), entityName, bpSearchData, cb);
    }

    public void getBioPortalSearchContentDetails(ProjectId projectId, BioPortalSearchData bpSearchData,
            BioPortalReferenceData bpRefData, AsyncCallback<String> cb) {
        proxy.getBioPortalSearchContentDetails(projectId.getId(), bpSearchData, bpRefData, cb);
    }

    public void createExternalReference(ProjectId projectId, String entityName, BioPortalReferenceData bpRefData,
            UserId userId, String operationDescription, AsyncCallback<EntityData> cb) {
        proxy.createExternalReference(projectId.getId(), entityName, bpRefData, userId.getUserName(), operationDescription, cb);
    }

    public void replaceExternalReference(ProjectId projectId, String entityName, BioPortalReferenceData bpRefData, EntityData oldValueEntityData,
            UserId userId, String operationDescription, AsyncCallback<EntityData> cb) {
        proxy.replaceExternalReference(projectId.getId(), entityName, bpRefData, oldValueEntityData, userId.getUserName(), operationDescription, cb);
    }


}
