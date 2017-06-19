package edu.stanford.bmir.protege.web.client.portlet;

import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.PropertyEntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.PropertyType;
import edu.stanford.bmir.protege.web.client.rpc.data.ValueType;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.*;

import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/05/15
 */
public class LegacyCompatUtil {

    /**
     * Convert EntityData to OWLEntityData.
     * @param entityData The entity data to convert.  Can be null (to deal with legacy code).  If null an absent
     *                   value will be returned.
     * @return The EntityData as OWLEntityData, or absent if the supplied entityData was null or if it is malformed.
     */
    public static Optional<OWLEntityData> toOWLEntityData(EntityData entityData) {
        if(entityData == null) {
            return Optional.empty();
        }
        if(entityData instanceof PropertyEntityData) {
            PropertyEntityData propertyEntityData = (PropertyEntityData) entityData;
            PropertyType propertyType = propertyEntityData.getPropertyType();
            if (propertyType != null) {
                switch(propertyType) {
                    case OBJECT:
                        return Optional.of(new OWLObjectPropertyData(DataFactory.getOWLObjectProperty(entityData.getName()), entityData.getBrowserText()));
                    case DATATYPE:
                        return Optional.of(new OWLDataPropertyData(DataFactory.getOWLDataProperty(entityData.getName()), entityData.getBrowserText()));
                    case ANNOTATION:
                        return Optional.of(new OWLAnnotationPropertyData(DataFactory.getOWLAnnotationProperty(entityData.getName()), entityData.getBrowserText()));

                }
            }
        }
        else if(entityData.getValueType() == ValueType.Cls) {
            return Optional.of(new OWLClassData(DataFactory.getOWLClass(entityData.getName()), entityData.getBrowserText()));
        }
        else if(entityData.getValueType() == ValueType.Instance) {
            return Optional.of(new OWLNamedIndividualData(DataFactory.getOWLNamedIndividual(entityData.getName()), entityData.getBrowserText()));
        }

        else if(entityData.getValueType() == ValueType.Property) {
            return Optional.empty();
        }
        return Optional.empty();
    }
}
