package edu.stanford.bmir.protege.web.shared.frame;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.HasSignature;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLEntity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

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
        this.dataProperty = checkNotNull(dataProperty);
        this.propertyValueList = checkNotNull(propertyValueList);
        this.domains = ImmutableSet.copyOf(checkNotNull(domains));
        this.ranges = ImmutableSet.copyOf(checkNotNull(ranges));
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
        signature.addAll(domains);
        signature.addAll(ranges);
        return signature;
    }

    public Set<OWLClass> getDomains() {
        return domains;
    }

    public Set<OWLDatatype> getRanges() {
        return ranges;
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


    @Override
    public String toString() {
        return toStringHelper("DataPropertyFrame")
                .addValue(dataProperty)
                .addValue(propertyValueList)
                .add("domains", domains)
                .add("ranges", ranges)
                .add("functional", functional)
                .toString();
    }


}
