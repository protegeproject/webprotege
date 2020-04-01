package edu.stanford.bmir.protege.web.shared.frame;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import javax.annotation.Nonnull;

import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-01
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class PlainObjectPropertyFrame extends PlainEntityFrame {

    public static PlainObjectPropertyFrame get(@Nonnull OWLObjectProperty subject,
                                               @Nonnull ImmutableSet<PlainPropertyAnnotationValue> annotationValues,
                                               @Nonnull ImmutableSet<ObjectPropertyCharacteristic> characteristics,
                                               @Nonnull ImmutableSet<OWLClass> domains,
                                               @Nonnull ImmutableSet<OWLClass> ranges,
                                               @Nonnull ImmutableSet<OWLObjectProperty> inverses) {
        return new AutoValue_PlainObjectPropertyFrame(subject, annotationValues, characteristics, domains, ranges, inverses);
    }

    public static PlainObjectPropertyFrame empty(OWLObjectProperty property) {
        return PlainObjectPropertyFrame.get(property, ImmutableSet.of(), ImmutableSet.of(), ImmutableSet.of(), ImmutableSet.of(), ImmutableSet.of());
    }

    @Nonnull
    @Override
    public abstract OWLObjectProperty getSubject();

    @Nonnull
    public abstract ImmutableSet<PlainPropertyAnnotationValue> getPropertyValues();

    @Nonnull
    public abstract ImmutableSet<ObjectPropertyCharacteristic> getCharacteristics();

    @Nonnull
    public abstract ImmutableSet<OWLClass> getDomains();

    @Nonnull
    public abstract ImmutableSet<OWLClass> getRanges();

    @Nonnull
    public abstract ImmutableSet<OWLObjectProperty> getInverseProperties();

    @Nonnull
    @Override
    public ObjectPropertyFrame toEntityFrame(FrameComponentRenderer renderer) {
        return ObjectPropertyFrame.get(
                renderer.getRendering(getSubject()),
                getPropertyValues().stream().map(pv -> pv.toPropertyValue(renderer)).collect(toImmutableSet()),
                getDomains().stream().map(renderer::getRendering).collect(toImmutableSet()),
                getRanges().stream().map(renderer::getRendering).collect(toImmutableSet()),
                getInverseProperties().stream().map(renderer::getRendering).collect(toImmutableSet()),
                getCharacteristics()
        );
    }

    @Nonnull
    @Override
    public PlainObjectPropertyFrame toPlainFrame() {
        return this;
    }
}
