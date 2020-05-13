package edu.stanford.bmir.protege.web.shared.frame;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableSet;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import javax.annotation.Nonnull;

import java.util.Comparator;

import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-03-31
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName(PlainNamedIndividualFrame.INDIVIDUAL_FRAME)
public abstract class PlainNamedIndividualFrame extends PlainEntityFrame {

    public static final String INDIVIDUAL_FRAME = "IndividualFrame";

    private static final String SAME_INDIVIDUAL = "sameIndividual";

    @JsonCreator
    @Nonnull
    public static PlainNamedIndividualFrame get(@Nonnull @JsonProperty(SUBJECT) OWLNamedIndividual subject,
                                                @Nonnull @JsonProperty(PARENTS) ImmutableSet<OWLClass> parents,
                                                @Nonnull @JsonProperty(SAME_INDIVIDUAL) ImmutableSet<OWLNamedIndividual> sameIndividuals,
                                                @Nonnull @JsonProperty(PROPERTY_VALUES) ImmutableSet<PlainPropertyValue> propertyValues) {
        return new AutoValue_PlainNamedIndividualFrame(subject,
                                                       parents,
                                                       sameIndividuals,
                                                       propertyValues);
    }

    public static PlainNamedIndividualFrame empty(OWLNamedIndividual individual) {
        return PlainNamedIndividualFrame.get(individual, ImmutableSet.of(), ImmutableSet.of(), ImmutableSet.of());
    }

    @Nonnull
    @Override
    public abstract OWLNamedIndividual getSubject();

    @Nonnull
    public abstract ImmutableSet<OWLClass> getParents();

    @JsonProperty(SAME_INDIVIDUAL)
    @Nonnull
    public abstract ImmutableSet<OWLNamedIndividual> getSameIndividuals();

    @Nonnull
    public abstract ImmutableSet<PlainPropertyValue> getPropertyValues();

    @Nonnull
    @Override
    public NamedIndividualFrame toEntityFrame(FrameComponentRenderer renderer,
                                              Comparator<PropertyValue> propertyValueComparator) {
        return NamedIndividualFrame.get(
                renderer.getRendering(getSubject()),
                getParents().stream()
                            .map(renderer::getRendering)
                            .collect(toImmutableSet()),
                getPropertyValues().stream().map(pv -> pv.toPropertyValue(renderer))
                                   .sorted(propertyValueComparator)
                                   .collect(toImmutableSet()),
                getSameIndividuals().stream().map(renderer::getRendering).collect(toImmutableSet())
        );
    }

    @Nonnull
    @Override
    public PlainNamedIndividualFrame toPlainFrame() {
        return this;
    }
}
