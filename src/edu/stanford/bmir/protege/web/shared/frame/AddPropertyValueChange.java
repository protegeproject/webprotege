package edu.stanford.bmir.protege.web.shared.frame;

import org.semanticweb.owlapi.model.OWLClass;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 10/12/2012
 */
public class AddPropertyValueChange extends ClassFrameChange {

    private PropertyValue propertyValue;

    public AddPropertyValueChange(OWLClass subject, PropertyValue propertyValue) {
        super(subject);
        this.propertyValue = propertyValue;
    }

    @Override
    public int hashCode() {
        return "AddPropertyValueChange".hashCode() + propertyValue.hashCode() + getSubject().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof AddPropertyValueChange)) {
            return false;
        }
        AddPropertyValueChange other = (AddPropertyValueChange) obj;
        return this.propertyValue.equals(other.propertyValue) && this.getSubject().equals(other.getSubject());
    }
}
