package edu.stanford.bmir.protege.web.shared.frame;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.HasSignature;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLDataPropertyData;
import edu.stanford.bmir.protege.web.shared.entity.OWLDatatypeData;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLEntity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
@SuppressWarnings("GwtInconsistentSerializableClass" )
public class DataPropertyFrame implements EntityFrame<OWLDataPropertyData>, HasSignature, Serializable, HasPropertyValueList, HasPropertyValues, HasAnnotationPropertyValues, HasLogicalPropertyValues  {

    private OWLDataPropertyData dataProperty;

    private PropertyValueList propertyValueList;

    private Set<OWLClassData> domains;

    private Set<OWLDatatypeData> ranges;

    private boolean functional;

    private DataPropertyFrame() {
    }

    public DataPropertyFrame(OWLDataPropertyData dataProperty, PropertyValueList propertyValueList, Set<OWLClassData> domains, Set<OWLDatatypeData> ranges, boolean functional) {
        this.dataProperty = checkNotNull(dataProperty);
        this.propertyValueList = checkNotNull(propertyValueList);
        this.domains = ImmutableSet.copyOf(checkNotNull(domains));
        this.ranges = ImmutableSet.copyOf(checkNotNull(ranges));
        this.functional = functional;
    }

    @Override
    public OWLDataPropertyData getSubject() {
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
        signature.add(dataProperty.getEntity());
        signature.addAll(propertyValueList.getSignature());
        signature.addAll(domains.stream().map(OWLClassData::getEntity).collect(toList()));
        signature.addAll(ranges.stream().map(OWLDatatypeData::getEntity).collect(toList()));
        return signature;
    }

    public Set<OWLClassData> getDomains() {
        return domains;
    }

    public Set<OWLDatatypeData> getRanges() {
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
