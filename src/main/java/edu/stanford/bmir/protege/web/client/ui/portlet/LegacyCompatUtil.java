package edu.stanford.bmir.protege.web.client.ui.portlet;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.PropertyEntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.PropertyType;
import edu.stanford.bmir.protege.web.client.rpc.data.ValueType;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.*;

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
            return Optional.absent();
        }
        if(entityData instanceof PropertyEntityData) {
            PropertyEntityData propertyEntityData = (PropertyEntityData) entityData;
            PropertyType propertyType = propertyEntityData.getPropertyType();
            if (propertyType != null) {
                switch(propertyType) {
                    case OBJECT:
                        return Optional.<OWLEntityData>of(new OWLObjectPropertyData(DataFactory.getOWLObjectProperty(entityData.getName()), entityData.getBrowserText()));
                    case DATATYPE:
                        return Optional.<OWLEntityData>of(new OWLDataPropertyData(DataFactory.getOWLDataProperty(entityData.getName()), entityData.getBrowserText()));
                    case ANNOTATION:
                        return Optional.<OWLEntityData>of(new OWLAnnotationPropertyData(DataFactory.getOWLAnnotationProperty(entityData.getName()), entityData.getBrowserText()));

                }
            }
        }
        else if(entityData.getValueType() == ValueType.Cls) {
            return Optional.<OWLEntityData>of(new OWLClassData(DataFactory.getOWLClass(entityData.getName()), entityData.getBrowserText()));
        }
        else if(entityData.getValueType() == ValueType.Instance) {
            return Optional.<OWLEntityData>of(new OWLNamedIndividualData(DataFactory.getOWLNamedIndividual(entityData.getName()), entityData.getBrowserText()));
        }

        else if(entityData.getValueType() == ValueType.Property) {
            return Optional.absent();
        }
        return Optional.absent();
    }
}
