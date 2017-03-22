package edu.stanford.bmir.protege.web.shared.frame;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.HasSignature;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLNamedIndividualData;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/12/2012
 */
@SuppressWarnings("GwtInconsistentSerializableClass" )
public class NamedIndividualFrame implements EntityFrame<OWLNamedIndividualData>, HasPropertyValues, HasAnnotationPropertyValues, HasLogicalPropertyValues, HasPropertyValueList, HasSignature, Serializable {

    private OWLNamedIndividualData subject;

    private ImmutableSet<OWLClassData> namedTypes;

    private PropertyValueList propertyValueList;

    private ImmutableSet<OWLNamedIndividualData> sameIndividuals;

    @GwtSerializationConstructor
    private NamedIndividualFrame() {
    }

    public NamedIndividualFrame(@Nonnull OWLNamedIndividualData subject,
                                @Nonnull Set<OWLClassData> namedTypes,
                                @Nonnull PropertyValueList propertyValueList,
                                @Nonnull Set<OWLNamedIndividualData> sameIndividuals) {
        this.subject = checkNotNull(subject);
        this.namedTypes = ImmutableSet.copyOf(checkNotNull(namedTypes));
        this.propertyValueList = checkNotNull(propertyValueList);
        this.sameIndividuals = ImmutableSet.copyOf(checkNotNull(sameIndividuals));
    }

    public OWLNamedIndividualData getSubject() {
        return subject;
    }

    public ImmutableSet<OWLClassData> getClasses() {
        return namedTypes;
    }

    public ImmutableSet<OWLNamedIndividualData> getSameIndividuals() {
        return sameIndividuals;
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
    public Set<PropertyValue> getPropertyValues() {
        return propertyValueList.getPropertyValues();
    }

    /**
     * Gets the signature of the object that implements this interface.
     * @return A set of entities that represent the signature of this object
     */
    @Override
    public Set<OWLEntity> getSignature() {
        Set<OWLEntity> result = new HashSet<>();
        result.add(subject.getEntity());
        result.addAll(namedTypes.stream().map(OWLClassData::getEntity).collect(toList()));
        result.addAll(propertyValueList.getSignature());
        result.addAll(sameIndividuals.stream().map(OWLNamedIndividualData::getEntity).collect(toList()));
        return result;
    }

    @Override
    public PropertyValueList getPropertyValueList() {
        return propertyValueList;
    }

    @Override
    public int hashCode() {
        return subject.hashCode() + namedTypes.hashCode() + propertyValueList.hashCode() + sameIndividuals.hashCode();//Objects.hashCode(subject, namedTypes, propertyValueList);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof NamedIndividualFrame)) {
            return false;
        }
        NamedIndividualFrame other = (NamedIndividualFrame) obj;
        return this.subject.equals(other.subject) && this.namedTypes.equals(other.namedTypes) && this.propertyValueList.equals(other.propertyValueList) && this.sameIndividuals.equals(other.sameIndividuals);
    }


    @Override
    public String toString() {
        return Objects.toStringHelper("NamedIndividualFrame")
                .addValue(subject)
                .add("types", namedTypes)
                .addValue(propertyValueList)
                .add("sameIndividuals", sameIndividuals)
                .toString();
    }

    public static class Builder {

        private OWLNamedIndividualData subject;

        private Set<OWLClassData> namedTypes = new HashSet<>();

        private List<PropertyValue> propertyValues = new ArrayList<>();

        private Set<OWLNamedIndividualData> sameIndividuals = new HashSet<>();

        public Builder(OWLNamedIndividualData subject) {
            this.subject = subject;
        }

        public void addClass(OWLClassData cls) {
            namedTypes.add(cls);
        }

        public void addSameIndividual(OWLNamedIndividualData individual) {
            this.sameIndividuals.add(individual);
        }

        public void addPropertyValue(PropertyValue propertyValue) {
            propertyValues.add(propertyValue);
        }

        public void addPropertyValues(Collection<PropertyValue> propertyValues) {
            this.propertyValues.addAll(propertyValues);
        }

        public NamedIndividualFrame build() {
            return new NamedIndividualFrame(subject, namedTypes, new PropertyValueList(propertyValues), sameIndividuals);
        }


    }
}
