package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
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
    private final RenderingManager rm;

    @Nonnull
    private final Provider<AxiomPropertyValueTranslator> axiomPropertyValueTranslatorProvider;

    @Inject
    public DataPropertyFrameTranslator(@Nonnull OWLOntology rootOntology,
                                       @Nonnull RenderingManager renderingManager,
                                       @Nonnull Provider<AxiomPropertyValueTranslator> axiomPropertyValueTranslatorProvider) {
        this.rootOntology = rootOntology;
        this.rm = renderingManager;
        this.axiomPropertyValueTranslatorProvider = axiomPropertyValueTranslatorProvider;
    }

    @Override
    public DataPropertyFrame getFrame(OWLDataPropertyData subject) {
        Set<OWLAxiom> propertyValueAxioms = new HashSet<>();
        Set<OWLClassData> domains = new HashSet<>();
        Set<OWLDatatypeData> ranges = new HashSet<>();
        boolean functional = false;
        for(OWLOntology ontology : rootOntology.getImportsClosure()) {
            propertyValueAxioms.addAll(ontology.getAnnotationAssertionAxioms(subject.getEntity().getIRI()));
            for(OWLDataPropertyDomainAxiom ax : ontology.getDataPropertyDomainAxioms(subject.getEntity())) {
                if(!ax.getDomain().isAnonymous()) {
                    domains.add(rm.getRendering(ax.getDomain().asOWLClass()));
                }
            }
            for(OWLDataPropertyRangeAxiom ax : ontology.getDataPropertyRangeAxioms(subject.getEntity())) {
                if(ax.getRange().isDatatype()) {
                    ranges.add(rm.getRendering(ax.getRange().asOWLDatatype()));
                }
            }
            if(EntitySearcher.isFunctional(subject.getEntity(), ontology)) {
                functional = true;
            }
        }
        Set<PropertyValue> propertyValues = new HashSet<>();
        AxiomPropertyValueTranslator translator = axiomPropertyValueTranslatorProvider.get();
        for(OWLAxiom ax : propertyValueAxioms) {
            propertyValues.addAll(translator.getPropertyValues(subject.getEntity(), ax, rootOntology, State.ASSERTED
            ));
        }

        PropertyValueList pvl = new PropertyValueList(propertyValues);
        return new DataPropertyFrame(subject, pvl, domains, ranges, functional);
    }

    @Override
    public Set<OWLAxiom> getAxioms(DataPropertyFrame frame, Mode mode) {
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
