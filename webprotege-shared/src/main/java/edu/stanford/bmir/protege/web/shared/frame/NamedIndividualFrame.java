package edu.stanford.bmir.protege.web.shared.frame;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLNamedIndividualData;

import javax.annotation.Nonnull;
import java.io.Serializable;

import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/12/2012
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class NamedIndividualFrame implements EntityFrame<OWLNamedIndividualData>, HasPropertyValues, HasAnnotationPropertyValues, HasLogicalPropertyValues, HasPropertyValueList, Serializable {

    @Nonnull
    public static NamedIndividualFrame get(@Nonnull OWLNamedIndividualData subject,
                                @Nonnull ImmutableSet<OWLClassData> namedTypes,
                                @Nonnull ImmutableSet<PropertyValue> propertyValueList,
                                @Nonnull ImmutableSet<OWLNamedIndividualData> sameIndividuals) {
        return new AutoValue_NamedIndividualFrame(subject,
                                                  namedTypes,
                                                  propertyValueList,
                                                  sameIndividuals);
    }

    @Nonnull
    public static NamedIndividualFrame empty(@Nonnull OWLNamedIndividualData subject) {
        return get(subject,
                   ImmutableSet.of(),
                   ImmutableSet.of(),
                   ImmutableSet.of());
    }

    @Nonnull
    public abstract OWLNamedIndividualData getSubject();

    @Nonnull
    public abstract ImmutableSet<OWLClassData> getClasses();

    @Nonnull
    @Override
    public abstract ImmutableSet<PropertyValue> getPropertyValues();

    @Nonnull
    public abstract ImmutableSet<OWLNamedIndividualData> getSameIndividuals();

    @Override
    public ImmutableSet<PropertyAnnotationValue> getAnnotationPropertyValues() {
        return getPropertyValueList().getAnnotationPropertyValues();
    }

    @Nonnull
    @Override
    public ImmutableList<PropertyValue> getLogicalPropertyValues() {
        return getPropertyValueList().getLogicalPropertyValues();
    }

    @Nonnull
    @Override
    public PropertyValueList getPropertyValueList() {
        return new PropertyValueList(getPropertyValues());
    }

    @Nonnull
    @Override
    public PlainNamedIndividualFrame toPlainFrame() {
        return PlainNamedIndividualFrame.get(
                getSubject().getEntity(),
                getClasses().stream().map(OWLClassData::getEntity).collect(toImmutableSet()),
                getSameIndividuals().stream().map(OWLNamedIndividualData::getEntity).collect(toImmutableSet()),
                getPropertyValues().stream().map(PropertyValue::toPlainPropertyValue).collect(toImmutableSet())
        );
    }
}
