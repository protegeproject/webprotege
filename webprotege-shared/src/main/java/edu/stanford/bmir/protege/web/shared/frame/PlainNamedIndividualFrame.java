package edu.stanford.bmir.protege.web.shared.frame;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import javax.annotation.Nonnull;

import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-03-31
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class PlainNamedIndividualFrame extends PlainEntityFrame {

    public static PlainNamedIndividualFrame get(@Nonnull OWLNamedIndividual subject,
                                                @Nonnull ImmutableSet<OWLClass> parents,
                                                @Nonnull ImmutableSet<OWLNamedIndividual> sameIndividuals,
                                                @Nonnull ImmutableSet<PlainPropertyValue> propertyValues) {
        return new AutoValue_PlainNamedIndividualFrame(subject,
                                                       parents,
                                                       sameIndividuals,
                                                       propertyValues);
    }

    @Nonnull
    @Override
    public abstract OWLNamedIndividual getSubject();

    @Nonnull
    public abstract ImmutableSet<OWLClass> getParents();

    @Nonnull
    public abstract ImmutableSet<OWLNamedIndividual> getSameIndividuals();

    @Nonnull
    @Override
    public abstract ImmutableSet<PlainPropertyValue> getPropertyValues();

    @Nonnull
    @Override
    public EntityFrame<? extends OWLEntityData> toEntityFrame(PrimitiveRenderer renderer) {
        return NamedIndividualFrame.get(
                renderer.getRendering(getSubject()),
                getParents().stream()
                            .map(renderer::getRendering)
                            .collect(toImmutableSet()),
                getPropertyValues().stream().map(pv -> pv.toPropertyValue(renderer)).collect(toImmutableSet()),
                getSameIndividuals().stream().map(renderer::getRendering).collect(toImmutableSet())
        );
    }
}
