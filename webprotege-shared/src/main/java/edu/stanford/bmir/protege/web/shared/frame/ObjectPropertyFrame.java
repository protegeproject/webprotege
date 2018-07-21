package edu.stanford.bmir.protege.web.shared.frame;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLObjectPropertyData;

import javax.annotation.Nonnull;
import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/12/2012
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class ObjectPropertyFrame implements EntityFrame<OWLObjectPropertyData>, HasAnnotationPropertyValues, Serializable {

    @Nonnull
    public static ObjectPropertyFrame get(@Nonnull OWLObjectPropertyData subject,
                                          @Nonnull ImmutableSet<PropertyAnnotationValue> annotationValues,
                                          @Nonnull ImmutableSet<OWLClassData> domains,
                                          @Nonnull ImmutableSet<OWLClassData> ranges,
                                          @Nonnull ImmutableSet<OWLObjectPropertyData> inverseProperties,
                                          @Nonnull ImmutableSet<ObjectPropertyCharacteristic> characteristics) {

        return new AutoValue_ObjectPropertyFrame(subject,
                                                 annotationValues,
                                                 domains,
                                                 ranges,
                                                 characteristics,
                                                 inverseProperties);
    }

    @Nonnull
    public abstract OWLObjectPropertyData getSubject();

    @Nonnull
    @Override
    public abstract ImmutableSet<PropertyAnnotationValue> getAnnotationPropertyValues();

    @Nonnull
    public abstract ImmutableSet<OWLClassData> getDomains();

    @Nonnull
    public abstract ImmutableSet<OWLClassData> getRanges();

    @Nonnull
    public abstract ImmutableSet<ObjectPropertyCharacteristic> getCharacteristics();

    @Nonnull
    public abstract ImmutableSet<OWLObjectPropertyData> getInverseProperties();
}
