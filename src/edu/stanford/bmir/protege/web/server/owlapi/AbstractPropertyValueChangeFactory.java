package edu.stanford.bmir.protege.web.server.owlapi;

import com.google.gwt.core.client.GWT;
import edu.stanford.bmir.protege.web.client.rpc.data.*;
import org.semanticweb.owlapi.model.*;

import java.util.List;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/04/2012
 */
public abstract  class AbstractPropertyValueChangeFactory extends OWLOntologyChangeFactory {

    private String entityName;
    
    private PropertyEntityData propertyEntityData;
    
    private EntityData entityData;

    protected AbstractPropertyValueChangeFactory(OWLAPIProject project, UserId userId, String changeDescription, String entityName, PropertyEntityData propertyEntityData, EntityData entityData) {
        super(project, userId, changeDescription);
        this.entityName = entityName;
        this.propertyEntityData = propertyEntityData;
        this.entityData = entityData;
    }

    /**
     * Normalises a malformed value to one corresponding to a literal.  An EntityData object is considered to be
     * malformed if it doesn't have any fields like valueType, propertyType etc. etc. set.  Unfortunately, some
     * of the UI code passes in malformed values.
     * @param value The value to normalise.
     * @return The normalised value, or the original value if it wasn't malformed.
     */
    protected EntityData normaliseValue(EntityData value) {
        // We need to normalise this because the UI sometimes don't fill out types etc. etc.
        RenderingManager rm = getRenderingManager();
        if(rm.isWellFormed(value)) {
            return value;
        }
        else {
            return rm.getEntityDataFromLexicalValue(value.getBrowserText());
        }
    }

    @Override
    final public void createChanges(final List<OWLOntologyChange> changeListToFill) {
        final RenderingManager rm = getRenderingManager();
        // The property type determines what happens
        String propertyName = propertyEntityData.getName() == null ? propertyEntityData.getBrowserText() : propertyEntityData.getName();
        Set<OWLEntity> entitiesMatchingProperty = rm.getEntities(propertyName);
        for(OWLEntity entity : entitiesMatchingProperty) {
            entity.accept(new OWLEntityVisitor() {
                public void visit(OWLClass cls) {
                    // Nope
                }

                public void visit(OWLObjectProperty property) {
                    // Yes
                    // Do the OWL THING
                    OWLNamedIndividual subject = rm.getEntity(entityName, EntityType.NAMED_INDIVIDUAL);
                    // Could be anon!
                    OWLNamedIndividual value = rm.getEntity(entityData, EntityType.NAMED_INDIVIDUAL);
                    createChanges(subject, property, value, changeListToFill);

                }

                public void visit(OWLDataProperty property) {
                    // Yes
                    OWLNamedIndividual subject = rm.getEntity(entityName, EntityType.NAMED_INDIVIDUAL);
                    OWLLiteral value = rm.getLiteral(entityData);
                    createChanges(subject, property, value, changeListToFill);
                }

                public void visit(OWLNamedIndividual individual) {
                    // Nope
                }

                public void visit(OWLDatatype datatype) {
                    // Nope
                }

                public void visit(OWLAnnotationProperty property) {
                    // Yes
                    IRI subject = rm.getIRI(entityName);
                    OWLAnnotationValue value = getAnnotationValue(entityData);
                    createChanges(subject, property, value, changeListToFill);
                }
            });
            
            
        }


        
    }

    protected OWLAnnotationProperty getAnnotationProperty(PropertyEntityData propertyEntityData) {
        PropertyType oldType = propertyEntityData.getPropertyType();
        if(oldType == null) {
            logUntypedProperty(propertyEntityData);
            propertyEntityData.setPropertyType(PropertyType.ANNOTATION);
        }
        else if(oldType != PropertyType.ANNOTATION) {
            logIncorrectlyTypedProperty(propertyEntityData);
            propertyEntityData.setPropertyType(PropertyType.ANNOTATION);
        }
        
        RenderingManager rm = getRenderingManager();
        OWLObject propertyObject = rm.getEntity(propertyEntityData, EntityType.ANNOTATION_PROPERTY);
        if(!(propertyObject instanceof OWLAnnotationProperty)) {
            throw new RuntimeException("Encountered " + propertyObject + " expected an annotation property.");
        }
        propertyEntityData.setPropertyType(oldType);
        return (OWLAnnotationProperty) propertyObject;
    }

    private static void logIncorrectlyTypedProperty(PropertyEntityData propertyEntityData) {
        GWT.log("WARNING: " + propertyEntityData + " is not an annotation property.  Coercing it into an annotation property.");
    }

    private static void logUntypedProperty(PropertyEntityData propertyEntityData) {
        GWT.log("WARNING: " + propertyEntityData + " does not have a property type specified for it! Coercing it into an annotation property.");
    }

    protected OWLAnnotationValue getAnnotationValue(EntityData entityData) {
        OWLAnnotationValue result;
        RenderingManager rm = getRenderingManager();

        if(!rm.isWellFormed(entityData)) {
            String name = entityData.getName() == null ? entityData.getBrowserText() : entityData.getName();
            if(name.contains(" ")) {
                // Assume not an IRI - i.e. a literal
                result = getRenderingManager().getLiteral(entityData);
            }
            else {
                IRI iri = getRenderingManager().getIRI(name);
                if(getRootOntology().containsEntityInSignature(iri, true)) {
                    result = iri;
                }
                else {
                    // Oh dear.  Assume a literal.
                    result = getRenderingManager().getLiteral(entityData);
                }
            }
        }
        else {
            OWLObject object = rm.getOWLObject(entityData);
            if(object instanceof OWLAnnotationValue) {
                result = (OWLAnnotationValue) object;
            }
            else if(object instanceof OWLEntity) {
                result = ((OWLEntity) object).getIRI();
            }
            else {
                throw new RuntimeException("Expected annotation value");
            }
        }
        return result;
    }

    protected abstract void createChanges(OWLAnnotationSubject subject, OWLAnnotationProperty property, OWLAnnotationValue value, List<OWLOntologyChange> changeListToFill);

    protected abstract void createChanges(OWLIndividual subject, OWLObjectProperty property, OWLIndividual value, List<OWLOntologyChange> changeListToFill);

    protected abstract void createChanges(OWLIndividual subject, OWLDataProperty property, OWLLiteral value, List<OWLOntologyChange> changeListToFill);



}
