package edu.stanford.bmir.protege.web.shared.frame;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLProperty;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/12/2012
 */
public class ObjectPropertyFrame implements EntityFrame<OWLObjectProperty>, HasAnnotationPropertyValues, Serializable {

    private OWLObjectProperty subject;

    private Set<PropertyAnnotationValue> annotationValues;

    private Set<OWLClass> domains;

    private Set<OWLClass> ranges;

    private ObjectPropertyFrame() {
    }

    public ObjectPropertyFrame(OWLObjectProperty subject, Set<PropertyAnnotationValue> annotationValues, Set<OWLClass> domains, Set<OWLClass> ranges, Set<OWLObjectProperty> inverseProperties) {
        this.subject = subject;
        this.annotationValues = new HashSet<PropertyAnnotationValue>(annotationValues);
        this.domains = new HashSet<OWLClass>(domains);
        this.ranges = new HashSet<OWLClass>(ranges);
    }

    public OWLObjectProperty getSubject() {
        return subject;
    }

    /**
     * Gets the signature of the object that implements this interface.
     * @return A set of entities that represent the signature of this object
     */
    @Override
    public Set<OWLEntity> getSignature() {
        Set<OWLEntity> signature = new HashSet<OWLEntity>();
        signature.add(subject);
        signature.addAll(domains);
        signature.addAll(ranges);
        return signature;
    }

    @Override
    public Set<PropertyAnnotationValue> getAnnotationPropertyValues() {
        return new HashSet<PropertyAnnotationValue>(annotationValues);
    }


    public static class Builder {

        private OWLObjectProperty subject;

        private Set<PropertyAnnotationValue> values = new HashSet<PropertyAnnotationValue>();

        private Set<OWLClass> domains = new HashSet<OWLClass>();

        private Set<OWLClass> ranges = new HashSet<OWLClass>();

        private Set<OWLObjectProperty> inverses = new HashSet<OWLObjectProperty>();

        public Builder(OWLObjectProperty subject) {
            this.subject = subject;
        }

        public void addPropertyValue(PropertyAnnotationValue propertyValue) {
            values.add(propertyValue);
        }

        public void addDomain(OWLClass domain) {
            domains.add(domain);
        }

        public void addRange(OWLClass range) {
            ranges.add(range);
        }

        public void clearDomains() {
            domains.clear();
        }

        public void clearRanges() {
            ranges.clear();
        }

        public ObjectPropertyFrame build() {
            return new ObjectPropertyFrame(subject, values, domains, ranges, inverses);
        }
    }

    public Set<OWLClass> getDomains() {
        return new HashSet<OWLClass>(domains);
    }

    public Set<OWLClass> getRanges() {
        return new HashSet<OWLClass>(ranges);
    }

    @Override
    public int hashCode() {
        return "PropertyFrame".hashCode() + subject.hashCode() + annotationValues.hashCode() + domains.hashCode() + ranges.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof ObjectPropertyFrame)) {
            return false;
        }
        ObjectPropertyFrame other = (ObjectPropertyFrame) obj;
        return this.subject.equals(other.subject) && this.annotationValues.equals(other.annotationValues) && this.domains.equals(other.domains) && this.ranges.equals(other.ranges);
    }
}
