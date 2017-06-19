package edu.stanford.bmir.protege.web.shared.frame;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.entity.OWLObjectPropertyData;
import org.semanticweb.owlapi.model.OWLEntity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/12/2012
 */
@SuppressWarnings("GwtInconsistentSerializableClass" )
public class ObjectPropertyFrame implements EntityFrame<OWLObjectPropertyData>, HasAnnotationPropertyValues, Serializable {

    private OWLObjectPropertyData subject;

    private Set<PropertyAnnotationValue> annotationValues;

    private Set<OWLClassData> domains;

    private Set<OWLClassData> ranges;

    private Set<OWLObjectPropertyData> inverses;

    private Set<ObjectPropertyCharacteristic> characteristics;

    private ObjectPropertyFrame() {
    }

    public ObjectPropertyFrame(OWLObjectPropertyData subject,
                               Set<PropertyAnnotationValue> annotationValues,
                               Set<OWLClassData> domains,
                               Set<OWLClassData> ranges,
                               Set<OWLObjectPropertyData> inverseProperties,
                               Set<ObjectPropertyCharacteristic> characteristics) {
        this.subject = checkNotNull(subject);
        this.annotationValues = new HashSet<>(checkNotNull(annotationValues));
        this.domains = new HashSet<>(checkNotNull(domains));
        this.ranges = new HashSet<>(checkNotNull(ranges));
        this.inverses = new HashSet<>(checkNotNull(inverseProperties));
        this.characteristics = new HashSet<>(checkNotNull(characteristics));
    }

    public OWLObjectPropertyData getSubject() {
        return subject;
    }

    /**
     * Gets the signature of the object that implements this interface.
     * @return A set of entities that represent the signature of this object
     */
    @Override
    public Set<OWLEntity> getSignature() {
        Set<OWLEntity> signature = new HashSet<OWLEntity>();
        signature.add(subject.getEntity());
        signature.addAll(domains.stream().map(OWLEntityData::getEntity).collect(toList()));
        signature.addAll(ranges.stream().map(OWLEntityData::getEntity).collect(toList()));
        signature.addAll(inverses.stream().map(OWLEntityData::getEntity).collect(toList()));
        for(PropertyAnnotationValue v : annotationValues) {
            signature.add(v.getProperty().getEntity());
            signature.addAll(v.getValue().getSignature());
        }
        return signature;
    }

    @Override
    public Set<PropertyAnnotationValue> getAnnotationPropertyValues() {
        return new HashSet<PropertyAnnotationValue>(annotationValues);
    }


    public static class Builder {

        private OWLObjectPropertyData subject;

        private Set<PropertyAnnotationValue> values = new HashSet<>();

        private Set<OWLClassData> domains = new HashSet<>();

        private Set<OWLClassData> ranges = new HashSet<>();

        private Set<OWLObjectPropertyData> inverses = new HashSet<>();

        private Set<ObjectPropertyCharacteristic> characteristics = new HashSet<>();

        public Builder(OWLObjectPropertyData subject) {
            this.subject = subject;
        }

//        public void addPropertyValue(PropertyAnnotationValue propertyValue) {
//            values.add(propertyValue);
//        }
//
//        public void addPropertyCharacteristic(ObjectPropertyCharacteristic characteristic) {
//            characteristics.add(characteristic);
//        }
//
//        public void addDomain(OWLClass domain) {
//            domains.add(domain);
//        }
//
//        public void addRange(OWLClass range) {
//            ranges.add(range);
//        }
//
//        public void clearDomains() {
//            domains.clear();
//        }
//
//        public void clearRanges() {
//            ranges.clear();
//        }

        public ObjectPropertyFrame build() {
            return new ObjectPropertyFrame(subject, values, domains, ranges, inverses, characteristics);
        }
    }

    public Set<OWLClassData> getDomains() {
        return new HashSet<>(domains);
    }

    public Set<OWLClassData> getRanges() {
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
