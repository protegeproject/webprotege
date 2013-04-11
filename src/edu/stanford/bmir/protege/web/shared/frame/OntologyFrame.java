package edu.stanford.bmir.protege.web.shared.frame;

import edu.stanford.bmir.protege.web.shared.HasSignature;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/01/2013
 */
public class OntologyFrame implements HasSignature, Serializable, HasPropertyValueList, HasPropertyValues, HasAnnotationPropertyValues {

    private OWLOntologyID subject;

    private PropertyValueList propertyValueList;

    private OntologyFrame() {
    }

    public OntologyFrame(OWLOntologyID subject, Collection<PropertyAnnotationValue> propertyValues) {
        this.subject = subject;
        this.propertyValueList = new PropertyValueList(Collections.<PropertyValue>unmodifiableCollection(propertyValues));
    }

    @Override
    public Set<PropertyAnnotationValue> getAnnotationPropertyValues() {
        return propertyValueList.getAnnotationPropertyValues();
    }

    @Override
    public PropertyValueList getPropertyValueList() {
        return propertyValueList;
    }

    @Override
    public Set<PropertyValue> getPropertyValues() {
        return propertyValueList.getPropertyValues();
    }

    /**
     * Gets the signature of the object that implements this interface.
     * @return A set of entities that represent the signature of this object
     */
    @Override
    public Set<OWLEntity> getSignature() {
        return propertyValueList.getSignature();
    }

    @Override
    public int hashCode() {
        return "OntologyFrame".hashCode() + subject.hashCode() + propertyValueList.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof OntologyFrame)) {
            return false;
        }
        OntologyFrame other = (OntologyFrame) obj;
        return this.subject.equals(other.subject) && this.propertyValueList.equals(other.propertyValueList);
    }
}
