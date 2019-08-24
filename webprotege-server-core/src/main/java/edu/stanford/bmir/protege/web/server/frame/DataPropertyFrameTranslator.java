package edu.stanford.bmir.protege.web.server.frame;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.index.*;
import edu.stanford.bmir.protege.web.server.renderer.ContextRenderer;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLDataPropertyData;
import edu.stanford.bmir.protege.web.shared.entity.OWLDatatypeData;
import edu.stanford.bmir.protege.web.shared.frame.DataPropertyFrame;
import edu.stanford.bmir.protege.web.shared.frame.PropertyAnnotationValue;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValue;
import edu.stanford.bmir.protege.web.shared.frame.State;
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
public class DataPropertyFrameTranslator implements FrameTranslator<DataPropertyFrame, OWLDataPropertyData> {

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
    private final ContextRenderer ren;

    @Nonnull
    private final Provider<AxiomPropertyValueTranslator> axiomPropertyValueTranslatorProvider;

    @Inject
    public DataPropertyFrameTranslator(@Nonnull ProjectOntologiesIndex projectOntologiesIndex,
                                       @Nonnull AnnotationAssertionAxiomsBySubjectIndex annotationAssertionAxiomsBySubjectId,
                                       @Nonnull DataPropertyDomainAxiomsIndex dataPropertyDomainAxiomsIndex,
                                       @Nonnull DataPropertyRangeAxiomsIndex dataPropertyRangeAxiomsIndex,
                                       @Nonnull DataPropertyCharacteristicsIndex dataPropertyCharacteristicsIndex,
                                       @Nonnull ContextRenderer ren,
                                       @Nonnull Provider<AxiomPropertyValueTranslator> axiomPropertyValueTranslatorProvider) {
        this.projectOntologiesIndex = projectOntologiesIndex;
        this.annotationAssertionsIndex = annotationAssertionAxiomsBySubjectId;
        this.domainAxiomsIndex = dataPropertyDomainAxiomsIndex;
        this.rangeAxiomsIndex = dataPropertyRangeAxiomsIndex;
        this.characteristicsIndex = dataPropertyCharacteristicsIndex;
        this.ren = ren;
        this.axiomPropertyValueTranslatorProvider = axiomPropertyValueTranslatorProvider;
    }

    @Nonnull
    @Override
    public DataPropertyFrame getFrame(@Nonnull OWLDataPropertyData subject) {

        var property = subject.getEntity();

        var propertyValueAxioms = projectOntologiesIndex
                .getOntologyIds()
                .flatMap(ontId -> annotationAssertionsIndex.getAxiomsForSubject(property.getIRI(), ontId))
                .collect(toImmutableSet());

        var domains = projectOntologiesIndex
                .getOntologyIds()
                .flatMap(ontId -> domainAxiomsIndex.getDataPropertyDomainAxioms(property, ontId))
                .map(OWLDataPropertyDomainAxiom::getDomain)
                .filter(OWLClassExpression::isNamed)
                .map(OWLClassExpression::asOWLClass)
                .map(ren::getClassData)
                .collect(toImmutableSet());

        var ranges = projectOntologiesIndex
                .getOntologyIds()
                .flatMap(ontId -> rangeAxiomsIndex.getDataPropertyRangeAxioms(property, ontId))
                .map(OWLDataPropertyRangeAxiom::getRange)
                .filter(OWLDataRange::isDatatype)
                .map(OWLDataRange::asOWLDatatype)
                .map(ren::getDatatypeData)
                .collect(toImmutableSet());


        var functional = projectOntologiesIndex
                .getOntologyIds()
                .anyMatch(ont -> characteristicsIndex.isFunctional(property, ont));


        ImmutableSet.Builder<PropertyValue> propertyValues = ImmutableSet.builder();
        AxiomPropertyValueTranslator translator = axiomPropertyValueTranslatorProvider.get();
        for(OWLAxiom ax : propertyValueAxioms) {
            propertyValues.addAll(translator.getPropertyValues(property, ax, State.ASSERTED));
        }
        return DataPropertyFrame.get(subject, propertyValues.build(), domains, ranges, functional);
    }

    @Nonnull
    @Override
    public Set<OWLAxiom> getAxioms(@Nonnull DataPropertyFrame frame,
                                   @Nonnull Mode mode) {
        Set<OWLAxiom> result = new HashSet<>();
        for(PropertyAnnotationValue pv : frame.getAnnotationPropertyValues()) {
            AxiomPropertyValueTranslator translator = axiomPropertyValueTranslatorProvider.get();
            result.addAll(translator.getAxioms(frame.getSubject().getEntity(), pv, mode));
        }
        for(OWLClassData domain : frame.getDomains()) {
            OWLAxiom ax = DataFactory
                    .get()
                    .getOWLDataPropertyDomainAxiom(frame.getSubject().getEntity(), domain.getEntity());
            result.add(ax);
        }
        for(OWLDatatypeData range : frame.getRanges()) {
            OWLAxiom ax = DataFactory
                    .get()
                    .getOWLDataPropertyRangeAxiom(frame.getSubject().getEntity(), range.getEntity());
            result.add(ax);
        }
        if(frame.isFunctional()) {
            result.add(DataFactory.get().getOWLFunctionalDataPropertyAxiom(frame.getSubject().getEntity()));
        }
        return result;
    }
}
