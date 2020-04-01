package edu.stanford.bmir.protege.web.shared.frame;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableSet;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;

import javax.annotation.Nonnull;

import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-01
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class PlainAnnotationPropertyFrame extends PlainEntityFrame {

    @Nonnull
    public static PlainAnnotationPropertyFrame get(@Nonnull OWLAnnotationProperty subject,
                                                   @Nonnull ImmutableSet<PlainPropertyAnnotationValue> annotations,
                                                   @Nonnull ImmutableSet<IRI> domains,
                                                   @Nonnull ImmutableSet<IRI> ranges) {
        return new AutoValue_PlainAnnotationPropertyFrame(subject, annotations, domains, ranges);
    }

    public static PlainAnnotationPropertyFrame empty(OWLAnnotationProperty property) {
        return PlainAnnotationPropertyFrame.get(property, ImmutableSet.of(), ImmutableSet.of(), ImmutableSet.of());
    }

    @Nonnull
    @Override
    public abstract OWLAnnotationProperty getSubject();

    @Nonnull
    public abstract ImmutableSet<PlainPropertyAnnotationValue> getPropertyValues();

    @Nonnull
    public abstract ImmutableSet<IRI> getDomains();

    @Nonnull
    public abstract ImmutableSet<IRI> getRanges();

    @Nonnull
    @Override
    public AnnotationPropertyFrame toEntityFrame(FrameComponentRenderer renderer) {
        return AnnotationPropertyFrame.get(
                renderer.getRendering(getSubject()),
                getPropertyValues().stream().map(pv -> pv.toPropertyValue(renderer)).collect(toImmutableSet()),
                getDomains().stream().flatMap(d -> renderer.getRendering(d).stream()).collect(toImmutableSet()),
                getRanges().stream().flatMap(r -> renderer.getRendering(r).stream()).collect(toImmutableSet())
        );
    }

    @Nonnull
    @Override
    public PlainAnnotationPropertyFrame toPlainFrame() {
        return this;
    }
}
