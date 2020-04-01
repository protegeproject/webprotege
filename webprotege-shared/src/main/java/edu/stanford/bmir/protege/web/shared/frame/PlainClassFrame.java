package edu.stanford.bmir.protege.web.shared.frame;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import org.semanticweb.owlapi.model.OWLClass;

import javax.annotation.Nonnull;

import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-03-31
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class PlainClassFrame extends PlainEntityFrame {


    @Nonnull
    public static PlainClassFrame get(@Nonnull OWLClass subject,
                                      @Nonnull ImmutableSet<OWLClass> classEntries,
                                      @Nonnull ImmutableSet<PlainPropertyValue> propertyValues) {

        return new AutoValue_PlainClassFrame(subject,
                                             classEntries,
                                             propertyValues);
    }

    public static PlainClassFrame empty(@Nonnull OWLClass subject) {
        return get(subject, ImmutableSet.of(), ImmutableSet.of());
    }

    /**
     * Gets the subject of this class frame.
     *
     * @return The subject.  Not {@code null}.
     */
    @Nonnull
    @Override
    public abstract OWLClass getSubject();

    @Nonnull
    public abstract ImmutableSet<OWLClass> getParents();

    /**
     * Gets the {@link PropertyValue}s in this frame.
     *
     * @return The (possibly empty) set of property values in this frame. Not {@code null}.  The returned set is unmodifiable.
     */
    @Nonnull
    public abstract ImmutableSet<PlainPropertyValue> getPropertyValues();

    @Nonnull
    public ClassFrame toEntityFrame(@Nonnull FrameComponentRenderer renderer) {
        OWLClassData subject = renderer.getRendering(getSubject());
        ImmutableSet<OWLClassData> parents = getParents().stream()
                                .map(renderer::getRendering)
                                .collect(toImmutableSet());
        ImmutableSet<PropertyValue> propertyValues = getPropertyValues()
                .stream()
                .map(pv -> pv.toPropertyValue(renderer))
                .collect(toImmutableSet());
        return ClassFrame.get(
                subject,
                parents,
                propertyValues
        );
    }

    @Nonnull
    @Override
    public PlainClassFrame toPlainFrame() {
        return this;
    }
}
