package edu.stanford.bmir.protege.web.shared.frame;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
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

    private ImmutableList<PropertyAnnotationValue> annotationValues;

    private Set<OWLClassData> domains;

    private Set<OWLClassData> ranges;

    private Set<OWLObjectPropertyData> inverses;

    private Set<ObjectPropertyCharacteristic> characteristics;

    private ObjectPropertyFrame() {
    }

    public ObjectPropertyFrame(OWLObjectPropertyData subject,
                               ImmutableList<PropertyAnnotationValue> annotationValues,
                               Set<OWLClassData> domains,
                               Set<OWLClassData> ranges,
                               Set<OWLObjectPropertyData> inverseProperties,
                               Set<ObjectPropertyCharacteristic> characteristics) {
        this.subject = checkNotNull(subject);
        this.annotationValues = checkNotNull(annotationValues);
        this.domains = new HashSet<>(checkNotNull(domains));
        this.ranges = new HashSet<>(checkNotNull(ranges));
        this.inverses = new HashSet<>(checkNotNull(inverseProperties));
        this.characteristics = new HashSet<>(checkNotNull(characteristics));
    }

    public OWLObjectPropertyData getSubject() {
        return subject;
    }

    @Override
    public ImmutableList<PropertyAnnotationValue> getAnnotationPropertyValues() {
        return annotationValues;
    }


    public static class Builder {

        private OWLObjectPropertyData subject;

        private ImmutableList.Builder<PropertyAnnotationValue> values = ImmutableList.builder();

        private Set<OWLClassData> domains = new HashSet<>();

        private Set<OWLClassData> ranges = new HashSet<>();

        private Set<OWLObjectPropertyData> inverses = new HashSet<>();

        private Set<ObjectPropertyCharacteristic> characteristics = new HashSet<>();

        public Builder(OWLObjectPropertyData subject) {
            this.subject = subject;
        }

        public ObjectPropertyFrame build() {
            return new ObjectPropertyFrame(subject, values.build(), domains, ranges, inverses, characteristics);
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
        return MoreObjects.toStringHelper("ObjectPropertyFrame")
                          .addValue(subject)
                          .add("annotations", annotationValues)
                          .add("domains", domains)
                          .add("ranges", ranges)
                          .add("inverses", inverses)
                          .add("characteristics", characteristics)
                          .toString();
    }
}
