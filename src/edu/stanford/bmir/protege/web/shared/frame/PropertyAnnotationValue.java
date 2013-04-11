package edu.stanford.bmir.protege.web.shared.frame;

import com.google.common.base.Optional;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/11/2012
 */
public final class PropertyAnnotationValue extends PropertyValue {

    private OWLEntity entity;

    private PropertyAnnotationValue() {
    }

    public PropertyAnnotationValue(OWLAnnotationProperty property, OWLAnnotationValue value) {
        super(property, value);
    }

    public PropertyAnnotationValue(OWLAnnotationProperty property, OWLEntity entityValue) {
        super(property, entityValue.getIRI());
        this.entity = entityValue;
    }

    @Override
    public OWLAnnotationProperty getProperty() {
        return (OWLAnnotationProperty) super.getProperty();
    }

    @Override
    public OWLAnnotationValue getValue() {
        return (OWLAnnotationValue) super.getValue();
    }

    public Optional<OWLEntity> getValueAsEntity() {
        if(entity == null) {
            return Optional.absent();
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
