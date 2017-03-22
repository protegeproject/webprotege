package edu.stanford.bmir.protege.web.shared.frame;

import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;

import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/11/2012
 */
public class PropertyAnnotationValue extends PropertyValue {


    @SuppressWarnings("GwtInconsistentSerializableClass" )
    private OWLEntityData entity;

    private PropertyAnnotationValue() {
    }

    public PropertyAnnotationValue(OWLAnnotationPropertyData property, OWLPrimitiveData value, PropertyValueState propertyValueState) {
        super(property, value, propertyValueState);
    }

    public PropertyAnnotationValue(OWLAnnotationPropertyData property, OWLEntityData entityValue, PropertyValueState propertyValueState) {
        super(property, entityValue, propertyValueState);
        this.entity = entityValue;
    }

    @Override
    public boolean isValueMostSpecific() {
        return true;
    }

    @Override
    public OWLAnnotationPropertyData getProperty() {
        return (OWLAnnotationPropertyData) super.getProperty();
    }

    @Override
    public OWLPrimitiveData getValue() {
        return super.getValue();
    }

    public Optional<OWLEntityData> getValueAsEntity() {
        if(entity == null) {
            return Optional.empty();
        }
        else {
            return Optional.of(entity);
        }
    }

    @Override
    public boolean isAnnotation() {
        return true;
    }

    @Override
    public boolean isLogical() {
        return false;
    }

    @Override
    protected PropertyValue duplicateWithState(PropertyValueState state) {
        return new PropertyAnnotationValue(getProperty(), getValue(), state);
    }

    @Override
    public <R, E extends Throwable> R accept(PropertyValueVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return "PropertyAnnotationValue".hashCode() + getProperty().hashCode() + getValue().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof PropertyAnnotationValue)) {
            return false;
        }
        PropertyAnnotationValue other = (PropertyAnnotationValue) obj;
        return this.getProperty().equals(other.getProperty()) && this.getValue().equals(other.getValue());
    }
}
