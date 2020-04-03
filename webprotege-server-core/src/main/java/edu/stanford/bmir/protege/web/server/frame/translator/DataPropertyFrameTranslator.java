package edu.stanford.bmir.protege.web.server.frame.translator;

import edu.stanford.bmir.protege.web.server.frame.Mode;
import edu.stanford.bmir.protege.web.server.index.*;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.frame.*;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class DataPropertyFrameTranslator {

    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Nonnull
    private final AnnotationAssertionAxiomsBySubjectIndex annotationAssertionsIndex;

    @Nonnull
    private final DataPropertyDomainAxiomsIndex domainAxiomsIndex;

    @Nonnull
    private final DataPropertyRangeAxiomsIndex rangeAxiomsIndex;

    @Nonnull
    private final DataPropertyCharacteristicsIndex characteristicsIndex;

    @Nonnull
    private final Provider<AxiomPropertyValueTranslator> axiomPropertyValueTranslatorProvider;

    @Nonnull
    private final PropertyValue2AxiomTranslator propertyValue2AxiomTranslator;

    @Inject
    public DataPropertyFrameTranslator(@Nonnull ProjectOntologiesIndex projectOntologiesIndex,
                                       @Nonnull AnnotationAssertionAxiomsBySubjectIndex annotationAssertionAxiomsBySubjectId,
                                       @Nonnull DataPropertyDomainAxiomsIndex dataPropertyDomainAxiomsIndex,
                                       @Nonnull DataPropertyRangeAxiomsIndex dataPropertyRangeAxiomsIndex,
                                       @Nonnull DataPropertyCharacteristicsIndex dataPropertyCharacteristicsIndex,
                                       @Nonnull Provider<AxiomPropertyValueTranslator> axiomPropertyValueTranslatorProvider,
                                       @Nonnull PropertyValue2AxiomTranslator propertyValue2AxiomTranslator) {
        this.projectOntologiesIndex = projectOntologiesIndex;
        this.annotationAssertionsIndex = annotationAssertionAxiomsBySubjectId;
        this.domainAxiomsIndex = dataPropertyDomainAxiomsIndex;
        this.rangeAxiomsIndex = dataPropertyRangeAxiomsIndex;
        this.characteristicsIndex = dataPropertyCharacteristicsIndex;
        this.axiomPropertyValueTranslatorProvider = axiomPropertyValueTranslatorProvider;
        this.propertyValue2AxiomTranslator = propertyValue2AxiomTranslator;
    }

    @Nonnull
    public PlainDataPropertyFrame getFrame(@Nonnull OWLDataProperty subject) {
        
        var propertyValueAxioms = projectOntologiesIndex
                .getOntologyIds()
                .flatMap(ontId -> annotationAssertionsIndex.getAxiomsForSubject(subject.getIRI(), ontId))
                .collect(toImmutableSet());

        var domains = projectOntologiesIndex
                .getOntologyIds()
                .flatMap(ontId -> domainAxiomsIndex.getDataPropertyDomainAxioms(subject, ontId))
                .map(OWLDataPropertyDomainAxiom::getDomain)
                .filter(OWLClassExpression::isNamed)
                .map(OWLClassExpression::asOWLClass)
                .collect(toImmutableSet());

        var ranges = projectOntologiesIndex
                .getOntologyIds()
                .flatMap(ontId -> rangeAxiomsIndex.getDataPropertyRangeAxioms(subject, ontId))
                .map(OWLDataPropertyRangeAxiom::getRange)
                .filter(OWLDataRange::isDatatype)
                .map(OWLDataRange::asOWLDatatype)
                .collect(toImmutableSet());


        var functional = projectOntologiesIndex
                .getOntologyIds()
                .anyMatch(ont -> characteristicsIndex.isFunctional(subject, ont));



        AxiomPropertyValueTranslator translator = axiomPropertyValueTranslatorProvider.get();

        var propertyValues  = propertyValueAxioms.stream()
                           .flatMap(ax -> translator.getPropertyValues(subject, ax, State.ASSERTED).stream())
                           .filter(PlainPropertyValue::isAnnotation)
                           .map(pv -> (PlainPropertyAnnotationValue) pv)
                           .collect(toImmutableSet());
        return PlainDataPropertyFrame.get(subject, propertyValues, domains, ranges, functional);
    }

    @Nonnull
    public Set<OWLAxiom> getAxioms(@Nonnull PlainDataPropertyFrame frame,
                                   @Nonnull Mode mode) {
        Set<OWLAxiom> result = new HashSet<>();
        for(PlainPropertyAnnotationValue pv : frame.getPropertyValues()) {
            result.addAll(propertyValue2AxiomTranslator.getAxioms(frame.getSubject(), pv, mode));
        }
        for(OWLClass domain : frame.getDomains()) {
            OWLAxiom ax = DataFactory
                    .get()
                    .getOWLDataPropertyDomainAxiom(frame.getSubject(), domain);
            result.add(ax);
        }
        for(OWLDatatype range : frame.getRanges()) {
            OWLAxiom ax = DataFactory
                    .get()
                    .getOWLDataPropertyRangeAxiom(frame.getSubject(), range);
            result.add(ax);
        }
        if(frame.isFunctional()) {
            result.add(DataFactory.get().getOWLFunctionalDataPropertyAxiom(frame.getSubject()));
        }
        return result;
    }
}
