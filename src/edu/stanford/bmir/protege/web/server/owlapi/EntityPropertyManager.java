package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.PropertyEntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.UserId;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 02/04/2012
 */
public abstract class EntityPropertyManager {
    
    private OWLAPIProject project;
    
    private UserId userId;

    private String entityName;

    protected EntityPropertyManager(OWLAPIProject project, UserId userId, String entityName) {
        this.project = project;
        this.userId = userId;
        this.entityName = entityName;
    }

    public void addPropertyValue(String operationDescription, PropertyEntityData propertyEntity, EntityData value) {
    }



    public void addPropertyValue(String projectName, String entityName, PropertyEntityData propertyEntity, EntityData value, String user, String operationDescription) {
    }

    public void removePropertyValue(String projectName, String entityName, PropertyEntityData propertyEntity, EntityData value, String user, String operationDescription) {
    }

    public void replacePropertyValue(String projectName, String entityName, PropertyEntityData propertyEntity, EntityData oldValue, EntityData newValue, String user, String operationDescription) {
    }

    public void setPropertyValues(String projectName, String entityName, PropertyEntityData propertyEntity, List<EntityData> values, String user, String operationDescription) {
    }
    
}
