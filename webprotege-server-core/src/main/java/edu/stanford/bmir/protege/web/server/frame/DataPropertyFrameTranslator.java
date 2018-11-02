package edu.stanford.bmir.protege.web.server.frame;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.renderer.ContextRenderer;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLDataPropertyData;
import edu.stanford.bmir.protege.web.shared.entity.OWLDatatypeData;
import edu.stanford.bmir.protege.web.shared.frame.*;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.search.EntitySearcher;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
public class DataPropertyFrameTranslator implements FrameTranslator<DataPropertyFrame, OWLDataPropertyData> {

    @Nonnull
    private final OWLOntology rootOntology;

    @Nonnull
    private final ContextRenderer ren;

    @Nonnull
    private final Provider<AxiomPropertyValueTranslator> axiomPropertyValueTranslatorProvider;

    @Inject
    public DataPropertyFrameTranslator(@Nonnull OWLOntology rootOntology,
                                       @Nonnull ContextRenderer ren,
                                       @Nonnull Provider<AxiomPropertyValueTranslator> axiomPropertyValueTranslatorProvider) {
        this.rootOntology = rootOntology;
        this.ren = ren;
        this.axiomPropertyValueTranslatorProvider = axiomPropertyValueTranslatorProvider;
    }

    @Nonnull
    @Override
    public DataPropertyFrame getFrame(@Nonnull OWLDataPropertyData subject) {
        Set<OWLAxiom> propertyValueAxioms = new HashSet<>();
        ImmutableSet.Builder<OWLClassData> domains = ImmutableSet.builder();
        ImmutableSet.Builder<OWLDatatypeData> ranges = ImmutableSet.builder();
        boolean functional = false;
        for(OWLOntology ontology : rootOntology.getImportsClosure()) {
            propertyValueAxioms.addAll(ontology.getAnnotationAssertionAxioms(subject.getEntity().getIRI()));
            for(OWLDataPropertyDomainAxiom ax : ontology.getDataPropertyDomainAxioms(subject.getEntity())) {
                if(!ax.getDomain().isAnonymous()) {
                    domains.add(ren.getClassData(ax.getDomain().asOWLClass()));
                }
            }
            for(OWLDataPropertyRangeAxiom ax : ontology.getDataPropertyRangeAxioms(subject.getEntity())) {
                if(ax.getRange().isDatatype()) {
                    ranges.add(ren.getDatatypeData(ax.getRange().asOWLDatatype()));
                }
            }
            if(EntitySearcher.isFunctional(subject.getEntity(), ontology)) {
                functional = true;
            }
        }
        ImmutableSet.Builder<PropertyValue> propertyValues = ImmutableSet.builder();
        AxiomPropertyValueTranslator translator = axiomPropertyValueTranslatorProvider.get();
        for(OWLAxiom ax : propertyValueAxioms) {
            propertyValues.addAll(translator.getPropertyValues(subject.getEntity(), ax, rootOntology, State.ASSERTED
            ));
        }
        return DataPropertyFrame.get(subject,
                                     propertyValues.build(),
                                     domains.build(),
                                     ranges.build(),
                                     functional);
    }

    @Nonnull
    @Override
    public Set<OWLAxiom> getAxioms(@Nonnull DataPropertyFrame frame, @Nonnull Mode mode) {
        Set<OWLAxiom> result = new HashSet<>();
        for(PropertyAnnotationValue pv : frame.getAnnotationPropertyValues()) {
            AxiomPropertyValueTranslator translator = axiomPropertyValueTranslatorProvider.get();
            result.addAll(translator.getAxioms(frame.getSubject().getEntity(), pv, mode));
        }
        for(OWLClassData domain : frame.getDomains()) {
            OWLAxiom ax = DataFactory.get().getOWLDataPropertyDomainAxiom(frame.getSubject().getEntity(), domain.getEntity());
            result.add(ax);
        }
        for(OWLDatatypeData range : frame.getRanges()) {
            OWLAxiom ax = DataFactory.get().getOWLDataPropertyRangeAxiom(frame.getSubject().getEntity(), range.getEntity());
            result.add(ax);
        }
        if(frame.isFunctional()) {
            result.add(DataFactory.get().getOWLFunctionalDataPropertyAxiom(frame.getSubject().getEntity()));
        }
        return result;
    }
}
