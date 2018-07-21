package edu.stanford.bmir.protege.web.shared.frame;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLNamedIndividualData;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/12/2012
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class NamedIndividualFrame implements EntityFrame<OWLNamedIndividualData>, HasPropertyValues, HasAnnotationPropertyValues, HasLogicalPropertyValues, HasPropertyValueList, Serializable {

    public static NamedIndividualFrame get(@Nonnull OWLNamedIndividualData subject,
                                @Nonnull ImmutableSet<OWLClassData> namedTypes,
                                @Nonnull ImmutableSet<PropertyValue> propertyValueList,
                                @Nonnull ImmutableSet<OWLNamedIndividualData> sameIndividuals) {
        return new AutoValue_NamedIndividualFrame(subject,
                                                  namedTypes,
                                                  ImmutableList.copyOf(propertyValueList),
                                                  sameIndividuals);
    }

    public abstract OWLNamedIndividualData getSubject();

    public abstract ImmutableSet<OWLClassData> getClasses();

    @Override
    public abstract ImmutableList<PropertyValue> getPropertyValues();

    public abstract ImmutableSet<OWLNamedIndividualData> getSameIndividuals();

    @Override
    public ImmutableList<PropertyAnnotationValue> getAnnotationPropertyValues() {
        return getPropertyValueList().getAnnotationPropertyValues();
    }

    @Override
    public ImmutableList<PropertyValue> getLogicalPropertyValues() {
        return getPropertyValueList().getLogicalPropertyValues();
    }

    @Override
    public PropertyValueList getPropertyValueList() {
        return new PropertyValueList(getPropertyValues());
    }

//    public static class Builder {
//
//        private OWLNamedIndividualData subject;
//
//        private Set<OWLClassData> namedTypes = new HashSet<>();
//
//        private List<PropertyValue> propertyValues = new ArrayList<>();
//
//        private Set<OWLNamedIndividualData> sameIndividuals = new HashSet<>();
//
//        public Builder(OWLNamedIndividualData subject) {
//            this.subject = subject;
//        }
//
//        public void addClass(OWLClassData cls) {
//            namedTypes.add(cls);
//        }
//
//        public void addSameIndividual(OWLNamedIndividualData individual) {
//            this.sameIndividuals.add(individual);
//        }
//
//        public void addPropertyValue(PropertyValue propertyValue) {
//            propertyValues.add(propertyValue);
//        }
//
//        public void addPropertyValues(Collection<PropertyValue> propertyValues) {
//            this.propertyValues.addAll(propertyValues);
//        }
//
//        public NamedIndividualFrame build() {
//            return new NamedIndividualFrame(subject, namedTypes, new PropertyValueList(propertyValues), sameIndividuals);
//        }
//
//
//    }
}
