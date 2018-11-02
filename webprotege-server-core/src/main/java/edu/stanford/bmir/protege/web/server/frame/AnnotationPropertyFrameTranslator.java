package edu.stanford.bmir.protege.web.server.frame;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.renderer.ContextRenderer;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.frame.AnnotationPropertyFrame;
import edu.stanford.bmir.protege.web.shared.frame.PropertyAnnotationValue;
import edu.stanford.bmir.protege.web.shared.frame.State;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.Imports;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: Matthew Horridge<br> Stanford University<br> Bio-Medical Informatics Research Group<br> Date: 23/04/2013
 */
public class AnnotationPropertyFrameTranslator implements FrameTranslator<AnnotationPropertyFrame, OWLAnnotationPropertyData> {

    @Nonnull
    private final ContextRenderer ren;

    @Nonnull
    private final OWLOntology rootOntology;

    @Inject
    public AnnotationPropertyFrameTranslator(@Nonnull ContextRenderer renderer,
                                             @Nonnull OWLOntology rootOntology) {
        this.ren = renderer;
        this.rootOntology = rootOntology;
    }

    @Nonnull
    @Override
    public AnnotationPropertyFrame getFrame(@Nonnull OWLAnnotationPropertyData subject) {
        ImmutableSet.Builder<PropertyAnnotationValue> propertyValues = ImmutableSet.builder();
        ImmutableSet.Builder<OWLEntityData> domains = ImmutableSet.builder();
        ImmutableSet.Builder<OWLEntityData> ranges = ImmutableSet.builder();
        for (OWLOntology ont : rootOntology.getImportsClosure()) {
            for (OWLAnnotationAssertionAxiom ax : ont.getAnnotationAssertionAxioms(subject.getEntity().getIRI())) {
                if (!(ax.getValue() instanceof OWLAnonymousIndividual)) {
                    propertyValues.add(PropertyAnnotationValue.get(ren.getAnnotationPropertyData(ax.getProperty()),
                                                                   ren.getAnnotationValueData(ax.getValue()),
                                                                   State.ASSERTED));
                }
            }
            for (OWLAnnotationPropertyDomainAxiom ax : ont.getAnnotationPropertyDomainAxioms(subject.getEntity())) {
                rootOntology.getEntitiesInSignature(ax.getDomain(), Imports.INCLUDED)
                            .stream()
                            .distinct()
                            .map(ren::getEntityData)
                            .sorted()
                            .forEach(domains::add);
            }
            for (OWLAnnotationPropertyRangeAxiom ax : ont.getAnnotationPropertyRangeAxioms(subject.getEntity())) {
                rootOntology.getEntitiesInSignature(ax.getRange(), Imports.INCLUDED)
                            .stream()
                            .distinct()
                            .map(ren::getEntityData)
                            .sorted()
                            .forEach(ranges::add);
            }
        }
        return AnnotationPropertyFrame.get(ren.getAnnotationPropertyData(subject.getEntity()),
                                           propertyValues.build(),
                                           domains.build(),
                                           ranges.build());
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
