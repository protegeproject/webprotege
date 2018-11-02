package edu.stanford.bmir.protege.web.server.frame;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
import edu.stanford.bmir.protege.web.server.renderer.ContextRenderer;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLNamedIndividualData;
import edu.stanford.bmir.protege.web.shared.frame.NamedIndividualFrame;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValue;
import edu.stanford.bmir.protege.web.shared.frame.State;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableSet.toImmutableSet;

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
    private final ContextRenderer rm;

    @Nonnull
    private final PropertyValueMinimiser propertyValueMinimiser;

    @Nonnull
    private final PropertyValueComparator propertyValueComparator;

    @Nonnull
    private final Provider<ClassFrameTranslator> translatorProvider;

    @Nonnull
    private final Provider<AxiomPropertyValueTranslator> axiomPropertyValueTranslatorProvider;

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Inject
    public NamedIndividualFrameTranslator(@Nonnull @RootOntology OWLOntology rootOntology,
                                          @Nonnull ContextRenderer rm,
                                          @Nonnull PropertyValueMinimiser propertyValueMinimiser,
                                          @Nonnull PropertyValueComparator propertyValueComparator,
                                          @Nonnull Provider<ClassFrameTranslator> translatorProvider,
                                          @Nonnull Provider<AxiomPropertyValueTranslator> axiomPropertyValueTranslatorProvider, @Nonnull OWLDataFactory dataFactory) {
        this.rootOntology = checkNotNull(rootOntology);
        this.rm = checkNotNull(rm);
        this.propertyValueMinimiser = checkNotNull(propertyValueMinimiser);
        this.propertyValueComparator = checkNotNull(propertyValueComparator);
        this.translatorProvider = checkNotNull(translatorProvider);
        this.axiomPropertyValueTranslatorProvider = checkNotNull(axiomPropertyValueTranslatorProvider);
        this.dataFactory = checkNotNull(dataFactory);
    }

    /**
     * Gets the entity type that this translator translates.
     * @return The entity type.  Not {@code null}.
     */
    @Override
    public EntityType<OWLNamedIndividual> getEntityType() {
        return EntityType.NAMED_INDIVIDUAL;
    }

    @Nonnull
    @Override
    public NamedIndividualFrame getFrame(@Nonnull OWLNamedIndividualData subject) {
        return translateToNamedIndividualFrame(subject);
    }

    @Nonnull
    @Override
    public Set<OWLAxiom> getAxioms(@Nonnull NamedIndividualFrame frame, @Nonnull Mode mode) {
        return translateToAxioms(frame.getSubject().getEntity(), frame, mode);
    }

    private NamedIndividualFrame translateToNamedIndividualFrame(OWLNamedIndividualData subject) {
        var relevantAxioms = getRelevantAxioms(subject.getEntity());
        ImmutableSet<OWLClassData> types = relevantAxioms.stream()
                      .filter(ax -> ax instanceof OWLClassAssertionAxiom)
                      .map(ax -> (OWLClassAssertionAxiom) ax)
                      .filter(ax -> !ax.getClassExpression().isAnonymous())
                      .map(ax -> ax.getClassExpression().asOWLClass())
                      .map(rm::getClassData)
                      .sorted()
                      .collect(toImmutableSet());

        final var propertyValues = new ArrayList<PropertyValue>();
        for(OWLAxiom axiom : relevantAxioms) {
            var translator = axiomPropertyValueTranslatorProvider.get();
            propertyValues.addAll(translator.getPropertyValues(subject.getEntity(), axiom, rootOntology, State.ASSERTED));
        }
        for(OWLOntology ont : rootOntology.getImportsClosure()) {
            for(OWLClassAssertionAxiom ax : ont.getClassAssertionAxioms(subject.getEntity())) {
                if(!ax.getClassExpression().isAnonymous()) {
                    var type = (OWLClass) ax.getClassExpression();
                    var classFrameTranslator = translatorProvider.get();
                    var classFrame = classFrameTranslator.getFrame(rm.getClassData(type));
                    for(PropertyValue propertyValue : classFrame.getPropertyValues()) {
                        // Bit yucky
                        if (!propertyValue.isAnnotation()) {
                            propertyValues.add(propertyValue.setState(State.DERIVED));
                        }
                    }
                }
            }
        }

        var propertyValuesMin = propertyValueMinimiser.minimisePropertyValues(propertyValues);
        propertyValuesMin.sort(propertyValueComparator);

        ImmutableSet<OWLNamedIndividualData> sameIndividuals = rootOntology.getImportsClosure().stream()
                    .flatMap(ont -> ont.getSameIndividualAxioms(subject.getEntity()).stream())
                    .flatMap(ax -> ax.getIndividuals().stream())
                    .filter(OWLIndividual::isNamed)
                    .filter(ind -> !ind.equals(subject.getEntity()))
                    .map(OWLIndividual::asOWLNamedIndividual)
                    .map(rm::getIndividualData)
                    .sorted()
                    .collect(toImmutableSet());

        return NamedIndividualFrame.get(subject,
                                        types,
                                        ImmutableSet.copyOf(propertyValuesMin),
                                        sameIndividuals);
    }

    private Set<OWLAxiom> getRelevantAxioms(OWLNamedIndividual subject) {
        var relevantAxioms = new HashSet<OWLAxiom>();
        for (OWLOntology ontology : rootOntology.getImportsClosure()) {
            relevantAxioms.addAll(ontology.getClassAssertionAxioms(subject));
            relevantAxioms.addAll(ontology.getAnnotationAssertionAxioms(subject.getIRI()));
            relevantAxioms.addAll(ontology.getObjectPropertyAssertionAxioms(subject));
            relevantAxioms.addAll(ontology.getDataPropertyAssertionAxioms(subject));
        }
        return relevantAxioms;
    }

    private Set<OWLAxiom> translateToAxioms(OWLNamedIndividual subject, NamedIndividualFrame frame, Mode mode) {
        var result = new HashSet<OWLAxiom>();
        for(OWLClassData cls : frame.getClasses()) {
            result.add(dataFactory.getOWLClassAssertionAxiom(cls.getEntity(), subject));
        }
        for(PropertyValue propertyValue : frame.getPropertyValues()) {
            var translator = axiomPropertyValueTranslatorProvider.get();
            result.addAll(translator.getAxioms(subject, propertyValue, mode));
        }
        for(OWLNamedIndividualData individual : frame.getSameIndividuals()) {
            result.add(dataFactory.getOWLSameIndividualAxiom(subject, individual.getEntity()));
        }
        return result;
    }
}
