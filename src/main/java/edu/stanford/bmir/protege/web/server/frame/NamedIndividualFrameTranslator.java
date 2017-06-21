package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLNamedIndividualData;
import edu.stanford.bmir.protege.web.shared.frame.ClassFrame;
import edu.stanford.bmir.protege.web.shared.frame.NamedIndividualFrame;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValue;
import edu.stanford.bmir.protege.web.shared.frame.State;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/12/2012
 */
public class NamedIndividualFrameTranslator implements EntityFrameTranslator<NamedIndividualFrame, OWLNamedIndividualData> {

    @Nonnull
    private final OWLOntology rootOntology;

    @Nonnull
    private final RenderingManager rm;

    @Nonnull
    private final PropertyValueMinimiser propertyValueMinimiser;

    @Nonnull
    private final PropertyValueComparator propertyValueComparator;

    @Nonnull
    private final Provider<ClassFrameTranslator> translatorProvider;

    @Inject
    public NamedIndividualFrameTranslator(@Nonnull @RootOntology OWLOntology rootOntology,
                                          @Nonnull RenderingManager rm,
                                          @Nonnull PropertyValueMinimiser propertyValueMinimiser,
                                          @Nonnull PropertyValueComparator propertyValueComparator,
                                          @Nonnull Provider<ClassFrameTranslator> translatorProvider) {
        this.rootOntology = rootOntology;
        this.rm = rm;
        this.propertyValueMinimiser = propertyValueMinimiser;
        this.propertyValueComparator = propertyValueComparator;
        this.translatorProvider = translatorProvider;
    }

    /**
     * Gets the entity type that this translator translates.
     * @return The entity type.  Not {@code null}.
     */
    @Override
    public EntityType<OWLNamedIndividual> getEntityType() {
        return EntityType.NAMED_INDIVIDUAL;
    }

    @Override
    public NamedIndividualFrame getFrame(OWLNamedIndividualData subject) {
        return translateToNamedIndividualFrame(subject);
    }

    @Override
    public Set<OWLAxiom> getAxioms(NamedIndividualFrame frame, Mode mode) {
        return translateToAxioms(frame.getSubject().getEntity(), frame, mode);
    }

    private NamedIndividualFrame translateToNamedIndividualFrame(OWLNamedIndividualData subject) {
        Set<OWLAxiom> relevantAxioms = getRelevantAxioms(subject.getEntity());

        NamedIndividualFrame.Builder builder = new NamedIndividualFrame.Builder(subject);
        for(OWLAxiom axiom : relevantAxioms) {
            if(axiom instanceof OWLClassAssertionAxiom) {
                OWLClassAssertionAxiom ax = (OWLClassAssertionAxiom) axiom;
                if(ax.getIndividual().equals(subject.getEntity()) && !ax.getClassExpression().isAnonymous()) {
                    builder.addClass(rm.getRendering(ax.getClassExpression().asOWLClass()));
                }
            }
        }
        List<PropertyValue> propertyValues = new ArrayList<>();
        for(OWLAxiom axiom : relevantAxioms) {
            AxiomPropertyValueTranslator translator = new AxiomPropertyValueTranslator();
            propertyValues.addAll(translator.getPropertyValues(subject.getEntity(), axiom, rootOntology, State.ASSERTED, rm));
        }
        for(OWLOntology ont : rootOntology.getImportsClosure()) {
            for(OWLClassAssertionAxiom ax : ont.getClassAssertionAxioms(subject.getEntity())) {
                if(!ax.getClassExpression().isAnonymous()) {
                    OWLClass type = (OWLClass) ax.getClassExpression();
                    ClassFrameTranslator classFrameTranslator = translatorProvider.get();
                    ClassFrame classFrame = classFrameTranslator.getFrame(rm.getRendering(type));
                    for(PropertyValue propertyValue : classFrame.getPropertyValues()) {
                        // Bit yucky
                        if (!propertyValue.isAnnotation()) {
                            propertyValues.add(propertyValue.setState(State.DERIVED));
                        }
                    }
                }
            }
        }

        propertyValues = propertyValueMinimiser.minimisePropertyValues(propertyValues);
        Collections.sort(propertyValues, propertyValueComparator);
        builder.addPropertyValues(propertyValues);
        for (OWLOntology ont : rootOntology.getImportsClosure()) {
            for(OWLSameIndividualAxiom sameIndividualAxiom : ont.getSameIndividualAxioms(subject.getEntity())) {
                final Set<OWLIndividual> individuals = sameIndividualAxiom.getIndividuals();
                for(OWLIndividual ind : individuals) {
                    if(!ind.isAnonymous() && !ind.equals(subject.getEntity())) {
                        builder.addSameIndividual(rm.getRendering(ind.asOWLNamedIndividual()));
                    }
                }
            }
        }

        return builder.build();
    }

    private Set<OWLAxiom> getRelevantAxioms(OWLNamedIndividual subject) {
        Set<OWLAxiom> relevantAxioms = new HashSet<>();
        for (OWLOntology ontology : rootOntology.getImportsClosure()) {
            relevantAxioms.addAll(ontology.getClassAssertionAxioms(subject));
            relevantAxioms.addAll(ontology.getAnnotationAssertionAxioms(subject.getIRI()));
            relevantAxioms.addAll(ontology.getObjectPropertyAssertionAxioms(subject));
            relevantAxioms.addAll(ontology.getDataPropertyAssertionAxioms(subject));
        }
        return relevantAxioms;
    }

    private Set<OWLAxiom> translateToAxioms(OWLNamedIndividual subject, NamedIndividualFrame frame, Mode mode) {
        Set<OWLAxiom> result = new HashSet<>();
        for(OWLClassData cls : frame.getClasses()) {
            result.add(DataFactory.get().getOWLClassAssertionAxiom(cls.getEntity(), subject));
        }
        for(PropertyValue propertyValue : frame.getPropertyValues()) {
            AxiomPropertyValueTranslator translator = new AxiomPropertyValueTranslator();
            result.addAll(translator.getAxioms(subject, propertyValue, mode));
        }
        for(OWLNamedIndividualData individual : frame.getSameIndividuals()) {
            result.add(DataFactory.get().getOWLSameIndividualAxiom(subject, individual.getEntity()));
        }
        return result;
    }
}
