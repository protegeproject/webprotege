package edu.stanford.bmir.protege.web.shared.frame;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.HasSignature;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import java.io.Serializable;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/12/2012
 */
public class NamedIndividualFrame implements EntityFrame<OWLNamedIndividual>, HasPropertyValues, HasAnnotationPropertyValues, HasLogicalPropertyValues, HasPropertyValueList, HasSignature, Serializable {

    private OWLNamedIndividual subject;

    private ImmutableSet<OWLClass> namedTypes;

    private PropertyValueList propertyValueList;

    private ImmutableSet<OWLNamedIndividual> sameIndividuals;

    private NamedIndividualFrame() {

    }

    public NamedIndividualFrame(OWLNamedIndividual subject, Set<OWLClass> namedTypes, PropertyValueList propertyValueList, Set<OWLNamedIndividual> sameIndividuals) {
        this.subject = checkNotNull(subject);
        this.namedTypes = ImmutableSet.copyOf(checkNotNull(namedTypes));
        this.propertyValueList = checkNotNull(propertyValueList);
        this.sameIndividuals = ImmutableSet.copyOf(checkNotNull(sameIndividuals));
    }

    public OWLNamedIndividual getSubject() {
        return subject;
    }

    public ImmutableSet<OWLClass> getClasses() {
        return namedTypes;
    }

    public ImmutableSet<OWLNamedIndividual> getSameIndividuals() {
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
        result.add(subject);
        result.addAll(namedTypes);
        result.addAll(propertyValueList.getSignature());
        result.addAll(sameIndividuals);
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

        private OWLNamedIndividual subject;

        private Set<OWLClass> namedTypes = new HashSet<>();

        private List<PropertyValue> propertyValues = new ArrayList<>();

        private Set<OWLNamedIndividual> sameIndividuals = new HashSet<OWLNamedIndividual>();

        public Builder(OWLNamedIndividual subject) {
            this.subject = subject;
        }

        public void addClass(OWLClass cls) {
            namedTypes.add(cls);
        }

        public void addSameIndividual(OWLNamedIndividual individual) {
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
