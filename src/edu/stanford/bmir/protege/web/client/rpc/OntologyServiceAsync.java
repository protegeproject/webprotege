package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.rpc.data.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.OWLClass;

import java.util.List;


/**
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 * @author Tania Tudorache <tudorache@stanford.edu>
 */
public interface OntologyServiceAsync {

    /*
     * Project management methods
     */

    void loadProject(String projectName, AsyncCallback<Integer> cb);

    void hasWritePermission(String projectName, String userName, AsyncCallback<Boolean> cb);

    /*
     * Ontology methods
     */
    void getImportedOntologies(String projectName, AsyncCallback<ImportsData> cb);

    void getMetrics(String projectName, AsyncCallback<List<MetricData>> cb);

    /*
     * Entity methods
     */

    void getEntityTriples(String projectName, String entityName, AsyncCallback<List<Triple>> cb);

    void getEntityTriples(String projectName, List<String> entities, List<String> properties, AsyncCallback<List<Triple>> cb);

    void getEntityTriples(String projectName, List<String> entities, List<String> properties, List<String> reifiedProps, AsyncCallback<List<Triple>> cb);

    void getEntityPropertyValues(String projectName, List<String> entities, List<String> properties, List<String> reifiedProps, AsyncCallback<List<EntityPropertyValues>> cb);

    void getRootEntity(String projectName, AsyncCallback<EntityData> cb);

    void renameEntity(String projectName, String oldName, String newName, String user, String operationDescription, AsyncCallback<EntityData> cb);

    void getEntity(String projectName, String entityName, AsyncCallback<EntityData> cb);

    /*
     * Class methods
     */

    void getSubclasses(String projectName, String className, AsyncCallback<List<SubclassEntityData>> cb);

    void getIndividuals(String projectName, String className, AsyncCallback<List<EntityData>> cb);

    void getIndividuals(String projectName, String className, int start, int limit, String sort, String dir,
            AsyncCallback<PaginationData<EntityData>> cb);

    void createClsWithProperty(ProjectId projectId, String clsName, OWLClass superCls, String propertyName, EntityData propertyValue, UserId userId, String operationDescription,
            AsyncCallback<EntityData> cb);

    void addSuperCls(String projectName, String clsName, String superClsName, String user, String operationDescription,
            AsyncCallback<Void> cb);

    void removeSuperCls(String projectName, String clsName, String superClsName, String user,
            String operationDescription, AsyncCallback<Void> cb);

    void moveCls(String projectName, String clsName, String oldParentName, String newParentName, boolean checkForCycles,
            String user,  String operationDescription, AsyncCallback<List<EntityData>> cb);

    void getRestrictionHtml(String projectName, String className, AsyncCallback<String> cb);

    void getClassConditions(String projectName, String className, AsyncCallback<List<ConditionItem>> cb);

    void deleteCondition(String projectName, String className, ConditionItem conditionItem, int row, String operationDescription,
            AsyncCallback<List<ConditionItem>> callback);

    void replaceCondition(String projectName, String className, ConditionItem conditionItem, int row,
            String newCondition, String operationDescription, AsyncCallback<List<ConditionItem>> callback);

    void addCondition(String projectName, String className, int row, String newCondition, boolean isNS,
            String operationDescription, AsyncCallback<List<ConditionItem>> callback);

    void getConditionAutocompleteSuggestions(String projectName, String condition, int cursorPosition,  AsyncCallback<ConditionSuggestion> callback);

    void getParents(String projectName, String className, boolean direct, AsyncCallback<List<EntityData>> callback);

    void getParentsHtml(String projectName, String className, boolean direct, AsyncCallback<String> callback);

    void getRelatedProperties(String projectName, String className, AsyncCallback<List<Triple>> callback);

    /*
     * Properties methods
     */

    void createObjectProperty(String projectName, String propertyName, String superPropName, String user,
            String operationDescription, AsyncCallback<EntityData> cb);

    void createDatatypeProperty(String projectName, String propertyName, String superPropName, String user,
            String operationDescription, AsyncCallback<EntityData> cb);

    void createAnnotationProperty(String projectName, String propertyName, String superPropName, String user,
            String operationDescription, AsyncCallback<EntityData> cb);

    void getSubproperties(String projectName, String propertyName, AsyncCallback<List<EntityData>> cb);

    void addPropertyValue(String projectName, String entityName, PropertyEntityData propertyEntity, EntityData value,
            String user, String operationDescription, AsyncCallback<Void> cb);

    void removePropertyValue(String projectName, String entityName, PropertyEntityData propertyEntity,
            EntityData value, String user, String operationDescription, AsyncCallback<Void> cb);

    void replacePropertyValue(String projectName, String entityName, PropertyEntityData propertyEntity,
            EntityData oldValue, EntityData newValue, String user, String operationDescription, AsyncCallback<Void> cb);

    void setPropertyValues(String projectName, String entityName, PropertyEntityData propertyEntity,
            List<EntityData> values, String user, String operationDescription, AsyncCallback<Void> cb);

    /*
     * Instance methods
     */

    void createInstance(String projectName, String instName, String typeName, String user, String operationDescription,
            AsyncCallback<EntityData> cb);

    void createInstanceValue(String projectName, String instName, String typeName, String subjectEntity,
            String propertyEntity, String user, String operationDescription, AsyncCallback<EntityData> cb);

    void createInstanceValueWithPropertyValue(String projectName, String instName, String typeName, String subjectEntity,
    		String propertyEntity, PropertyEntityData instancePropertyEntity, EntityData valueEntityData, String user,
    		String operationDescription, AsyncCallback<EntityData> callback);

    /*
     * Search
     */

    void search(String projectName, String searchString, ValueType valueType, int start, int limit, String sort,
            String dir, AsyncCallback<PaginationData<EntityData>> cb);

    void search(String projectName, String searchString, AsyncCallback<List<EntityData>> cb);

    void search(String projectName, String searchString, ValueType valueType, AsyncCallback<List<EntityData>> cb);

    void getPathToRoot(String projectName, String entityName, AsyncCallback<List<EntityData>> cb);

    /*
     * Util methods
     */

    void getBioPortalSearchContent(String projectName, String entityName, BioPortalSearchData bpSearchData,
            AsyncCallback<String> cb);

    void getBioPortalSearchContentDetails(String projectName, BioPortalSearchData bpSearchData,
            BioPortalReferenceData bpRefData, AsyncCallback<String> cb);

    void createExternalReference(String projectName, String entityName, BioPortalReferenceData bpRefData, String user,
            String operationDescription, AsyncCallback<EntityData> cb);

    void replaceExternalReference(String projectName, String entityName, BioPortalReferenceData bpRefData,
                                  EntityData oldValueEntityData,
                                  String user, String operationDescription, AsyncCallback<EntityData> async);

    void getDirectTypes(String projectName, String instanceName, AsyncCallback<List<EntityData>> cb);


}
