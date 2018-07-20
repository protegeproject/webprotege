package edu.stanford.bmir.protege.web.shared.frame;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.HasSignature;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/11/2012
 * <p>
 * A class frame describes some class in terms of other classEntries and property values.
 * </p>
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class ClassFrame implements EntityFrame<OWLClassData>, Serializable, HasPropertyValueList, HasPropertyValues, HasAnnotationPropertyValues, HasLogicalPropertyValues {

    /**
     * Gets the subject of this class frame.
     *
     * @return The subject.  Not {@code null}.
     */
    public abstract OWLClassData getSubject();

    public abstract ImmutableList<OWLClassData> getClassEntries();

    /**
     * Gets the {@link PropertyValue}s in this frame.
     *
     * @return The (possibly empty) set of property values in this frame. Not {@code null}.  The returned set is unmodifiable.
     */
    public abstract ImmutableList<PropertyValue> getPropertyValues();


    public static ClassFrame get(@Nonnull OWLClassData subject,
                                 @Nonnull Collection<OWLClassData> classEntries,
                                 @Nonnull Collection<PropertyValue> propertyValues) {
        return ClassFrame.builder(subject)
                         .setClassEntries(ImmutableList.copyOf(classEntries))
                         .setPropertyValues(ImmutableList.copyOf(propertyValues))
                         .build();
    }


    @AutoValue.Builder
    public static abstract class Builder {

        public abstract Builder setSubject(OWLClassData subject);

        public abstract Builder setClassEntries(List<OWLClassData> classEntries);

        public abstract Builder setPropertyValues(List<PropertyValue> propertyValues);

        public abstract ClassFrame build();
    }

    @Nonnull
    public static Builder builder(OWLClassData subject) {
        return new AutoValue_ClassFrame.Builder()
                .setSubject(subject)
                .setClassEntries(ImmutableList.of())
                .setPropertyValues(ImmutableList.of());
    }


    @Override
    public PropertyValueList getPropertyValueList() {
        return new PropertyValueList(getPropertyValues());
    }

    public ImmutableList<PropertyAnnotationValue> getAnnotationPropertyValues() {
        return getPropertyValueList().getAnnotationPropertyValues();
    }

    public ImmutableList<PropertyValue> getLogicalPropertyValues() {
        return getPropertyValueList().getLogicalPropertyValues();
    }
}
