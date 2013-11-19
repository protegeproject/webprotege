package edu.stanford.bmir.protege.web.shared.frame;

import org.semanticweb.owlapi.model.OWLClass;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 10/12/2012
 */
public class RemovePropertyValueChange extends ClassFrameChange {

    private PropertyValue propertyValue;

    public RemovePropertyValueChange(OWLClass subject, PropertyValue propertyValue) {
        super(subject);
        this.propertyValue = propertyValue;
    }

    public PropertyValue getPropertyValue() {
        return propertyValue;
    }

    @Override
    public int hashCode() {
        return "RemovePropertyValueChange".hashCode() + propertyValue.hashCode() + getSubject().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof RemovePropertyValueChange)) {
            return false;
        }
        RemovePropertyValueChange other = (RemovePropertyValueChange) obj;
        return propertyValue.equals(other.propertyValue) && getSubject().equals(other.getSubject());
    }

}
