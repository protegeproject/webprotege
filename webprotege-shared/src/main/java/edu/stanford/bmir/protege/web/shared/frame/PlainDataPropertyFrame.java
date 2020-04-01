package edu.stanford.bmir.protege.web.shared.frame;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDatatype;

import javax.annotation.Nonnull;

import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-01
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class PlainDataPropertyFrame extends PlainEntityFrame {

    public static PlainDataPropertyFrame get(@Nonnull OWLDataProperty subject,
                                             @Nonnull ImmutableSet<PlainPropertyAnnotationValue> annotations,
                                             @Nonnull ImmutableSet<OWLClass> domains,
                                             @Nonnull ImmutableSet<OWLDatatype> ranges,
                                             boolean functional) {
        return new AutoValue_PlainDataPropertyFrame(subject, annotations, domains, ranges, functional);
    }

    public static PlainDataPropertyFrame empty(OWLDataProperty property) {
        return PlainDataPropertyFrame.get(property, ImmutableSet.of(), ImmutableSet.of(), ImmutableSet.of(), false);
    }

    @Nonnull
    @Override
    public abstract OWLDataProperty getSubject();

    @Nonnull
    public abstract ImmutableSet<PlainPropertyAnnotationValue> getPropertyValues();

    @Nonnull
    public abstract ImmutableSet<OWLClass> getDomains();

    @Nonnull
    public abstract ImmutableSet<OWLDatatype> getRanges();

    public abstract boolean isFunctional();

    @Nonnull
    @Override
    public DataPropertyFrame toEntityFrame(FrameComponentRenderer renderer) {
        return DataPropertyFrame.get(
                renderer.getRendering(getSubject()),
                getPropertyValues().stream().map(pv -> pv.toPropertyValue(renderer)).collect(toImmutableSet()),
                getDomains().stream().map(renderer::getRendering).collect(toImmutableSet()),
                getRanges().stream().map(renderer::getRendering).collect(toImmutableSet()),
                isFunctional()
        );
    }

    @Nonnull
    @Override
    public PlainDataPropertyFrame toPlainFrame() {
        return this;
    }
}
