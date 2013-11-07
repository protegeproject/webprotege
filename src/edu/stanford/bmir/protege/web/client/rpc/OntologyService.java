package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import edu.stanford.bmir.protege.web.client.rpc.data.*;

import java.util.List;


/**
 * A service for accessing ontology data.
 * <p />
 *
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 * @author Tania Tudorache <tudorache@stanford.edu>
 */
@RemoteServiceRelativePath("ontology")
public interface OntologyService extends RemoteService {

    /*
     * Project management methods
     */


    /**
     * Loads a project.
     * @param projectName The name of the project to load.
     * @return An integer representing the version of the project.
     */
    public Integer loadProject(String projectName);

    public Boolean hasWritePermission(String projectName, String userName);


    public ImportsData getImportedOntologies(String projectName);

    public List<MetricData> getMetrics(String projectName);

    /*
     * Entity methods
     */


    public List<Triple> getEntityTriples(String projectName, String entityName);

    public List<Triple> getEntityTriples(String projectName, List<String> entities, List<String> properties);

    public List<Triple> getEntityTriples(String projectName, List<String> entities, List<String> properties, List<String> reifiedProps);

    public List<EntityPropertyValues> getEntityPropertyValues(String projectName, List<String> entities, List<String> properties, List<String> reifiedProps);

    public EntityData renameEntity(String projectName, String oldName, String newName, String user,
            String operationDescription);

    public EntityData getRootEntity(String projectName);

    public EntityData getEntity(String projectName, String entityName);

    public List<SubclassEntityData> getSubclasses(String projectName, String className);

    public List<EntityData> moveCls(String projectName, String clsName, String oldParentName, String newParentName, boolean checkForCycles,
            String user, String operationDescription);

    public PaginationData<EntityData> getIndividuals(String projectName, String className, int start, int limit, String sort, String dir);

    public String getRestrictionHtml(String projectName, String className);

    public List<ConditionItem> getClassConditions(String projectName, String className);

    public List<ConditionItem> deleteCondition(String projectName, String className, ConditionItem conditionItem, int row,
            String operationDescription);

    public List<ConditionItem> replaceCondition(String projectName, String className, ConditionItem conditionItem, int row, String newCondition,
            String operationDescription);

    public List<ConditionItem> addCondition(String projectName, String className, int row, String newCondition, boolean isNS,
            String operationDescription);

    public ConditionSuggestion getConditionAutocompleteSuggestions(String projectName, String condition, int cursorPosition);

    public List<EntityData> getParents(String projectName, String className, boolean direct);

    /**
     * Used by the "related properties" table.
     */
    public List<Triple> getRelatedProperties(String projectName, String className);

    /*
     * Properties methods
     */

    public List<EntityData> getSubproperties(String projectName, String propertyName);

    public void addPropertyValue(String projectName, String entityName, PropertyEntityData propertyEntity,
            EntityData value, String user, String operationDescription);

    public void removePropertyValue(String projectName, String entityName, PropertyEntityData propertyEntity,
            EntityData value, String user, String operationDescription);

    public void replacePropertyValue(String projectName, String entityName, PropertyEntityData propertyEntity,
            EntityData oldValue, EntityData newValue, String user, String operationDescription);

    void setPropertyValues(String projectName, String entityName, PropertyEntityData propertyEntity,
            List<EntityData> values, String user, String operationDescription);

    /*
     * Instance methods
     */

    public EntityData createInstance(String projectName, String instName, String typeName, String user,
            String operationDescription);

    public EntityData createInstanceValue(String projectName, String instName, String typeName, String subjectEntity,
            String propertyEntity, String user, String operationDescription);

    public EntityData createInstanceValueWithPropertyValue(String projectName, String instName, String typeName, String subjectEntity,
    		String propertyEntity, PropertyEntityData instancePropertyEntity, EntityData valueEntityData, String user, String operationDescription);

    public List<EntityData> getDirectTypes(String projectName, String instanceName);

    /*
     * Search
     */

    public PaginationData<EntityData> search(String projectName, String searchString, ValueType valueType, int start, int limit, String sort, String dir);

    public List<EntityData> search(String projectName, String searchString);

    public List<EntityData> search(String projectName, String searchString, ValueType valueType);

    public List<EntityData> getPathToRoot(String projectName, String entityName);

    /*
     * Util methods
     */

    public String getBioPortalSearchContent(String projectName, String entityName, BioPortalSearchData bpSearchData);

    public String getBioPortalSearchContentDetails(String projectName, BioPortalSearchData bpSearchData,
            BioPortalReferenceData bpRefData);

    public EntityData createExternalReference(String projectName, String entityName, BioPortalReferenceData bpRefData,
            String user, String operationDescription);

    public EntityData replaceExternalReference(String projectName, String entityName, BioPortalReferenceData bpRefData,
                                        EntityData oldValueEntityData,
                                        String user, String operationDescription);


}
