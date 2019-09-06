package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.server.index.*;
import edu.stanford.bmir.protege.web.server.renderer.ContextRenderer;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.frame.AnnotationPropertyFrame;
import edu.stanford.bmir.protege.web.shared.frame.PropertyAnnotationValue;
import edu.stanford.bmir.protege.web.shared.frame.State;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Author: Matthew Horridge<br> Stanford University<br> Bio-Medical Informatics Research Group<br> Date: 23/04/2013
 */
public class AnnotationPropertyFrameTranslator implements FrameTranslator<AnnotationPropertyFrame, OWLAnnotationPropertyData> {

    @Nonnull
    private final ContextRenderer ren;

    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Nonnull
    private final AnnotationAssertionAxiomsBySubjectIndex annotationAssertionsIndex;

    @Nonnull
    private final AnnotationPropertyDomainAxiomsIndex domainAxiomsIndex;

    @Nonnull
    private final AnnotationPropertyRangeAxiomsIndex rangeAxiomsIndex;

    @Nonnull
    private final EntitiesInProjectSignatureByIriIndex entitiesIndex;

    @Inject
    public AnnotationPropertyFrameTranslator(@Nonnull ContextRenderer renderer,
                                             @Nonnull ProjectOntologiesIndex projectOntologiesIndex,
                                             @Nonnull AnnotationAssertionAxiomsBySubjectIndex annotationAssertionsIndex,
                                             @Nonnull AnnotationPropertyDomainAxiomsIndex domainAxiomsIndex,
                                             @Nonnull AnnotationPropertyRangeAxiomsIndex rangeAxiomsIndex,
                                             @Nonnull EntitiesInProjectSignatureByIriIndex entitiesIndex) {
        this.ren = renderer;
        this.projectOntologiesIndex = projectOntologiesIndex;
        this.annotationAssertionsIndex = annotationAssertionsIndex;
        this.domainAxiomsIndex = domainAxiomsIndex;
        this.rangeAxiomsIndex = rangeAxiomsIndex;
        this.entitiesIndex = entitiesIndex;
    }

    @Nonnull
    @Override
    public AnnotationPropertyFrame getFrame(@Nonnull OWLAnnotationPropertyData subject) {
        var property = subject.getEntity();
        var propertyIRI = property.getIRI();
        var propertyValues = projectOntologiesIndex.getOntologyIds()
                .flatMap(ontId -> annotationAssertionsIndex.getAxiomsForSubject(propertyIRI, ontId))
                .filter(isTranslatableValue())
                .distinct()
                .map(toPropertyValue())
                .collect(toImmutableSet());

        var domains = projectOntologiesIndex.getOntologyIds()
                .flatMap(ontId -> domainAxiomsIndex.getAnnotationPropertyDomainAxioms(property, ontId))
                .map(OWLAnnotationPropertyDomainAxiom::getDomain)
                .flatMap(entitiesIndex::getEntitiesInSignature)
                .distinct()
                .map(ren::getEntityData)
                .sorted()
                .collect(toImmutableSet());

        var ranges = projectOntologiesIndex.getOntologyIds()
                .flatMap(ontId -> rangeAxiomsIndex.getAnnotationPropertyRangeAxioms(property, ontId))
                .map(OWLAnnotationPropertyRangeAxiom::getRange)
                .flatMap(entitiesIndex::getEntitiesInSignature)
                .distinct()
                .map(ren::getEntityData)
                .sorted()
                .collect(toImmutableSet());

        return AnnotationPropertyFrame.get(ren.getAnnotationPropertyData(subject.getEntity()),
                                           propertyValues,
                                           domains,
                                           ranges);
    }

    /**
     * Determines whether the annotation value is translatable (must be a literal or IRI)
     */
    private Predicate<OWLAnnotationAssertionAxiom> isTranslatableValue() {
        return ax -> !(ax.getValue() instanceof OWLAnonymousIndividual);
    }

    /**
     * Converts annotation assertion axioms to property values
     */
    private Function<OWLAnnotationAssertionAxiom, PropertyAnnotationValue> toPropertyValue() {
        return ax -> PropertyAnnotationValue.get(
                ren.getAnnotationPropertyData(ax.getProperty()),
                ren.getAnnotationValueData(ax.getValue()),
                State.ASSERTED
        );
    }

    @Nonnull
    @Override
    public Set<OWLAxiom> getAxioms(@Nonnull AnnotationPropertyFrame frame, @Nonnull Mode mode) {
        Set<OWLAxiom> result = new HashSet<>();
        for (PropertyAnnotationValue value : frame.getPropertyValues()) {
            value.getValue()
                 .asAnnotationValue()
                 .ifPresent(
                         annotationValue ->
                                 result.add(DataFactory.get().getOWLAnnotationAssertionAxiom(value.getProperty().getEntity(),
                                                                                             frame.getSubject().getEntity().getIRI(),
                                                                                             annotationValue)));
        }
        for (OWLEntityData domain : frame.getDomains()) {
            result.add(DataFactory.get().getOWLAnnotationPropertyDomainAxiom(frame.getSubject().getEntity(), domain.getEntity().getIRI()));
        }
        for (OWLEntityData range : frame.getRanges()) {
            result.add(DataFactory.get().getOWLAnnotationPropertyRangeAxiom(frame.getSubject().getEntity(), range.getEntity().getIRI()));
        }
        return result;
    }
}
