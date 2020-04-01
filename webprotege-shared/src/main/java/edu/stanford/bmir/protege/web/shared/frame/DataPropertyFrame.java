package edu.stanford.bmir.protege.web.shared.frame;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLDataPropertyData;
import edu.stanford.bmir.protege.web.shared.entity.OWLDatatypeData;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

import javax.annotation.Nonnull;
import java.io.DataOutput;
import java.io.Serializable;

import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class DataPropertyFrame implements EntityFrame<OWLDataPropertyData>, Serializable, HasPropertyValueList, HasPropertyValues, HasAnnotationPropertyValues, HasLogicalPropertyValues {

    @Nonnull
    public static DataPropertyFrame get(@Nonnull OWLDataPropertyData subject,
                                        @Nonnull ImmutableSet<PropertyValue> propertyValues,
                                        @Nonnull ImmutableSet<OWLClassData> domains,
                                        @Nonnull ImmutableSet<OWLDatatypeData> ranges,
                                        boolean functional) {
        return new AutoValue_DataPropertyFrame(subject,
                                               propertyValues,
                                               domains,
                                               ranges,
                                               functional);
    }

    @Nonnull
    public static DataPropertyFrame empty(@Nonnull OWLDataPropertyData subject) {
        return get(subject,
                   ImmutableSet.of(),
                   ImmutableSet.of(),
                   ImmutableSet.of(),
                   false);
    }

    @Override
    public abstract OWLDataPropertyData getSubject();

    @Override
    public abstract ImmutableSet<PropertyValue> getPropertyValues();

    @Nonnull
    public abstract ImmutableSet<OWLClassData> getDomains();

    @Nonnull
    public abstract ImmutableSet<OWLDatatypeData> getRanges();

    public abstract boolean isFunctional();

    @Override
    public PropertyValueList getPropertyValueList() {
        return new PropertyValueList(getPropertyValues());
    }

    @Override
    public ImmutableSet<PropertyAnnotationValue> getAnnotationPropertyValues() {
        return getPropertyValueList().getAnnotationPropertyValues();
    }

    @Override
    public ImmutableList<PropertyValue> getLogicalPropertyValues() {
        return getPropertyValueList().getLogicalPropertyValues();
    }

    @Nonnull
    @Override
    public PlainDataPropertyFrame toPlainFrame() {
        return PlainDataPropertyFrame.get(getSubject().getEntity(),
                                          getAnnotationPropertyValues().stream().map(PropertyAnnotationValue::toPlainPropertyValue).collect(toImmutableSet()),
                                          getDomains().stream().map(OWLClassData::getEntity).collect(toImmutableSet()),
                                          getRanges().stream().map(OWLDatatypeData::getEntity).collect(toImmutableSet()),
                                          isFunctional());
    }
}
