package edu.stanford.bmir.protege.web.shared.frame;

import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.HashSet;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/11/2012
 */
@SuppressWarnings("GwtInconsistentSerializableClass" )
public class AnnotationPropertyFrame implements EntityFrame<OWLAnnotationPropertyData>, HasPropertyValueList {

    private OWLAnnotationPropertyData subject;


    private PropertyValueList propertyValues;

    private Set<OWLEntityData> domains;

    private Set<OWLEntityData> ranges;

    private AnnotationPropertyFrame() {
    }

    public AnnotationPropertyFrame(OWLAnnotationPropertyData subject, Set<PropertyAnnotationValue> propertyValues, Set<OWLEntityData> domains, Set<OWLEntityData> ranges) {
        this.subject = subject;
        this.propertyValues = new PropertyValueList(propertyValues);
        this.domains = new HashSet<>(domains);
        this.ranges = new HashSet<>(ranges);
    }

    public OWLAnnotationPropertyData getSubject() {
        return subject;
    }

    @Override
    public Set<OWLEntity> getSignature() {
        Set<OWLEntity> sig = new HashSet<>();
        sig.add(subject.getEntity());
        sig.addAll(propertyValues.getSignature());
        domains.stream().map(OWLEntityData::getEntity).forEach(sig::add);
        ranges.stream().map(OWLEntityData::getEntity).forEach(sig::add);
        return sig;
    }

    @Override
    public PropertyValueList getPropertyValueList() {
        return propertyValues;
    }

    public Set<PropertyAnnotationValue> getPropertyValues() {
        return propertyValues.getAnnotationPropertyValues();
    }

    public Set<OWLEntityData> getDomains() {
        return new HashSet<>(domains);
    }

    public Set<OWLEntityData> getRanges() {
        return new HashSet<>(ranges);
    }

    @Override
    public int hashCode() {
        return "AnnotationPropertyFrame".hashCode() + subject.hashCode() + propertyValues.hashCode() + domains.hashCode() + ranges.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof AnnotationPropertyFrame)) {
            return false;
        }
        AnnotationPropertyFrame other = (AnnotationPropertyFrame) obj;
        return this.subject.equals(other.subject) && this.propertyValues.equals(other.propertyValues) && this.domains.equals(other.domains) && this.ranges.equals(other.ranges);
    }

    //    public Builder builder() {
//        return new Builder(subject, propertyValues);
//    }

//    public static class Builder {
//
//        private Set<PropertyAnnotationValue> propertyValues = new HashSet<PropertyAnnotationValue>();
//
//        private OWLAnnotationSubject subject;
//
//        public Builder(OWLAnnotationSubject subject, Set<PropertyAnnotationValue> propertyValues) {
//            this.subject = subject;
//            this.propertyValues.addAll(propertyValues);
//        }
//
//        public void addPropertyValue(OWLAnnotationProperty property, OWLAnnotationValue value) {
//            propertyValues.add(new PropertyAnnotationValue(property, value));
//        }
//
//        public void addPropertyValue(OWLAnnotationProperty property, String value) {
//            propertyValues.add(new PropertyAnnotationValue(property, DataFactory.getOWLLiteral(value)));
//        }
//
//        public void addPropertyValue(OWLAnnotationProperty property, int value) {
//            propertyValues.add(new PropertyAnnotationValue(property, DataFactory.getOWLLiteral(value)));
//        }
//
//        public void addPropertyValue(OWLAnnotationProperty property, double value) {
//            propertyValues.add(new PropertyAnnotationValue(property, DataFactory.getOWLLiteral(value)));
//        }
//
//        public AnnotationPropertyFrame build() {
//            return new AnnotationPropertyFrame(subject, propertyValues);
//        }
//
//    }

}
