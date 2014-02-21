package edu.stanford.bmir.protege.web.shared.frame;

import edu.stanford.bmir.protege.web.shared.HasSignature;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import java.io.Serializable;
import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/12/2012
 */
public class NamedIndividualFrame implements EntityFrame<OWLNamedIndividual>, HasPropertyValues, HasAnnotationPropertyValues, HasLogicalPropertyValues, HasPropertyValueList, HasSignature, Serializable {

    private OWLNamedIndividual subject;

    private Set<OWLClass> namedTypes;

    private PropertyValueList propertyValueList;

    private Set<OWLNamedIndividual> sameIndividuals;

    private NamedIndividualFrame() {

    }

    public NamedIndividualFrame(OWLNamedIndividual subject, Set<OWLClass> namedTypes, Collection<PropertyValue> propertyValueList, Set<OWLNamedIndividual> sameIndividuals) {
        this.subject = subject;
        this.namedTypes = new HashSet<OWLClass>(namedTypes);
        this.propertyValueList = new PropertyValueList(propertyValueList);
        this.sameIndividuals = new HashSet<OWLNamedIndividual>(sameIndividuals);
    }

    public OWLNamedIndividual getSubject() {
        return subject;
    }

    public Set<OWLClass> getClasses() {
        return namedTypes;
    }

    public Set<OWLNamedIndividual> getSameIndividuals() {
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
        Set<OWLEntity> result = new HashSet<OWLEntity>();
        result.add(subject);
        result.addAll(namedTypes);
        result.addAll(propertyValueList.getSignature());
        result.addAll(sameIndividuals);
        return result;
//        return Sets.union(namedTypes, propertyValueList.getSignature());
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
        StringBuilder sb = new StringBuilder();
        sb.append("NamedIndividualFrame");
        sb.append("(");
        sb.append(subject);
        sb.append(" NamedTypes(");
        sb.append(namedTypes);
        sb.append(") PropertyValues(");
        sb.append(propertyValueList);
        sb.append(") SameAs(");
        sb.append(sameIndividuals);
        sb.append("))");
        return sb.toString();
//        return Objects.toStringHelper(this).add("subject", subject).add("types", namedTypes).add("property values", propertyValueList).toString();
    }

    public static class Builder {

        private OWLNamedIndividual subject;

        private Set<OWLClass> namedTypes = new HashSet<OWLClass>();

        private List<PropertyValue> propertyValues = new ArrayList<PropertyValue>();

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
            return new NamedIndividualFrame(subject, namedTypes, propertyValues, sameIndividuals);
        }


    }
}
