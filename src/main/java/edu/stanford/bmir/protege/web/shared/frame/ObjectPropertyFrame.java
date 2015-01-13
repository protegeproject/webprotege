package edu.stanford.bmir.protege.web.shared.frame;

import com.google.common.base.Objects;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

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

    private Set<OWLObjectProperty> inverses;

    private Set<ObjectPropertyCharacteristic> characteristics;

    private ObjectPropertyFrame() {
    }

    public ObjectPropertyFrame(OWLObjectProperty subject, Set<PropertyAnnotationValue> annotationValues, Set<OWLClass> domains, Set<OWLClass> ranges, Set<OWLObjectProperty> inverseProperties, Set<ObjectPropertyCharacteristic> characteristics) {
        this.subject = checkNotNull(subject);
        this.annotationValues = new HashSet<>(checkNotNull(annotationValues));
        this.domains = new HashSet<>(checkNotNull(domains));
        this.ranges = new HashSet<>(checkNotNull(ranges));
        this.inverses = new HashSet<>(checkNotNull(inverseProperties));
        this.characteristics = new HashSet<>(checkNotNull(characteristics));
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
        signature.addAll(inverses);
        for(PropertyAnnotationValue v : annotationValues) {
            signature.add(v.getProperty());
            signature.addAll(v.getValue().getSignature());
        }
        return signature;
    }

    @Override
    public Set<PropertyAnnotationValue> getAnnotationPropertyValues() {
        return new HashSet<PropertyAnnotationValue>(annotationValues);
    }


    public static class Builder {

        private OWLObjectProperty subject;

        private Set<PropertyAnnotationValue> values = new HashSet<>();

        private Set<OWLClass> domains = new HashSet<>();

        private Set<OWLClass> ranges = new HashSet<>();

        private Set<OWLObjectProperty> inverses = new HashSet<>();

        private Set<ObjectPropertyCharacteristic> characteristics = new HashSet<>();

        public Builder(OWLObjectProperty subject) {
            this.subject = subject;
        }

        public void addPropertyValue(PropertyAnnotationValue propertyValue) {
            values.add(propertyValue);
        }

        public void addPropertyCharacteristic(ObjectPropertyCharacteristic characteristic) {
            characteristics.add(characteristic);
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
            return new ObjectPropertyFrame(subject, values, domains, ranges, inverses, characteristics);
        }
    }

    public Set<OWLClass> getDomains() {
        return new HashSet<>(domains);
    }

    public Set<OWLClass> getRanges() {
        return new HashSet<>(ranges);
    }

    public Set<ObjectPropertyCharacteristic> getCharacteristics() {
        return new HashSet<>(characteristics);
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


    @Override
    public String toString() {
        return Objects.toStringHelper("ObjectPropertyFrame")
                .addValue(subject)
                .add("annotations", annotationValues)
                .add("domains", domains)
                .add("ranges", ranges)
                .add("inverses", inverses)
                .add("characteristics", characteristics)
                .toString();
    }
}
