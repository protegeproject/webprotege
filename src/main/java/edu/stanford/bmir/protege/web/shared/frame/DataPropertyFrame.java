package edu.stanford.bmir.protege.web.shared.frame;

import edu.stanford.bmir.protege.web.shared.HasSignature;
import org.semanticweb.owlapi.model.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class DataPropertyFrame implements EntityFrame<OWLDataProperty>, HasSignature, Serializable, HasPropertyValueList, HasPropertyValues, HasAnnotationPropertyValues, HasLogicalPropertyValues  {

    private OWLDataProperty dataProperty;

    private PropertyValueList propertyValueList;

    private Set<OWLClass> domains;

    private Set<OWLDatatype> ranges;

    private boolean functional;

    private DataPropertyFrame() {
    }

    public DataPropertyFrame(OWLDataProperty dataProperty, PropertyValueList propertyValueList, Set<OWLClass> domains, Set<OWLDatatype> ranges, boolean functional) {
        this.dataProperty = dataProperty;
        this.propertyValueList = propertyValueList;
        this.domains = domains;
        this.ranges = ranges;
        this.functional = functional;
    }

    @Override
    public OWLDataProperty getSubject() {
        return dataProperty;
    }

    @Override
    public Set<PropertyAnnotationValue> getAnnotationPropertyValues() {
        return propertyValueList.getAnnotationPropertyValues();
    }

    @Override
    public Set<PropertyValue> getLogicalPropertyValues() {
        return propertyValueList.getLogicalPropertyValues();
    }

    @Override
    public PropertyValueList getPropertyValueList() {
        return propertyValueList;
    }

    @Override
    public Set<PropertyValue> getPropertyValues() {
        return propertyValueList.getPropertyValues();
    }

    @Override
    public Set<OWLEntity> getSignature() {
        Set<OWLEntity> signature = new HashSet<OWLEntity>();
        signature.add(dataProperty);
        signature.addAll(propertyValueList.getSignature());
        return signature;
    }

    public Set<OWLClass> getDomains() {
        return new HashSet<OWLClass>(domains);
    }

    public Set<OWLDatatype> getRanges() {
        return new HashSet<OWLDatatype>(ranges);
    }

    public boolean isFunctional() {
        return functional;
    }

    @Override
    public int hashCode() {
        return "DataPropertyFrame".hashCode() + dataProperty.hashCode() + propertyValueList.hashCode() + domains.hashCode() + ranges.hashCode() + (functional ? 1 : 0);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof DataPropertyFrame)) {
            return false;
        }
        DataPropertyFrame other = (DataPropertyFrame) obj;
        return this.dataProperty.equals(other.dataProperty) && this.propertyValueList.equals(other.propertyValueList) && this.domains.equals(other.domains) && this.ranges.equals(other.ranges) && this.functional == other.functional;
    }


}
