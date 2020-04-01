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
import java.util.stream.Collectors;

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


    @Nonnull
    public static ClassFrame get(@Nonnull OWLClassData subject,
                                 @Nonnull ImmutableSet<OWLClassData> classEntries,
                                 @Nonnull ImmutableSet<PropertyValue> propertyValues) {

        return new AutoValue_ClassFrame(subject,
                                        classEntries,
                                        propertyValues);
    }

    public static ClassFrame empty(@Nonnull OWLClassData subject) {
        return get(subject, ImmutableSet.of(), ImmutableSet.of());
    }

    /**
     * Gets the subject of this class frame.
     *
     * @return The subject.  Not {@code null}.
     */
    @Nonnull
    public abstract OWLClassData getSubject();

    @Nonnull
    public abstract ImmutableSet<OWLClassData> getClassEntries();

    /**
     * Gets the {@link PropertyValue}s in this frame.
     *
     * @return The (possibly empty) set of property values in this frame. Not {@code null}.  The returned set is unmodifiable.
     */
    @Nonnull
    public abstract ImmutableSet<PropertyValue> getPropertyValues();


    @Nonnull
    @Override
    public PropertyValueList getPropertyValueList() {
        return new PropertyValueList(getPropertyValues());
    }

    @Nonnull
    public ImmutableSet<PropertyAnnotationValue> getAnnotationPropertyValues() {
        return getPropertyValueList().getAnnotationPropertyValues();
    }

    @Nonnull
    public ImmutableList<PropertyValue> getLogicalPropertyValues() {
        return getPropertyValueList().getLogicalPropertyValues();
    }

    @Override
    @Nonnull
    public PlainClassFrame toPlainFrame() {
        return PlainClassFrame.get(
                getSubject().getEntity(),
                getClassEntries().stream().map(OWLClassData::getEntity).collect(toImmutableSet()),
                getPropertyValues().stream().map(PropertyValue::toPlainPropertyValue).collect(toImmutableSet())
        );
    }
}
