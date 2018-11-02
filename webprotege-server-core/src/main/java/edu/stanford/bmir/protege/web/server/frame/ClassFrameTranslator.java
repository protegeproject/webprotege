package edu.stanford.bmir.protege.web.server.frame;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.hierarchy.HasGetAncestors;
import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
import edu.stanford.bmir.protege.web.server.renderer.ContextRenderer;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.frame.ClassFrame;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValue;
import edu.stanford.bmir.protege.web.shared.frame.State;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/12/2012
 * <p>
 * A translator that converts sets of axioms to class frames and vice-versa.
 * </p>
 */
public class ClassFrameTranslator implements EntityFrameTranslator<ClassFrame, OWLClassData> {

    @Nonnull
    private final ContextRenderer ren;

    @Nonnull
    private final OWLOntology rootOntology;

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Nonnull
    private final HasGetAncestors<OWLClass> ancestorsProvider;

    @Nonnull
    private final PropertyValueMinimiser propertyValueMinimiser;

    @Nonnull
    private final PropertyValueComparator propertyValueComparator;

    @Nonnull
    private final Provider<AxiomPropertyValueTranslator> axiomPropertyValueTranslatorProvider;

    private boolean includeAncestorFrames = false;

    @Inject
    public ClassFrameTranslator(@Nonnull ContextRenderer renderer,
                                @Nonnull @RootOntology OWLOntology rootOntology,
                                @Nonnull OWLDataFactory dataFactory,
                                @Nonnull HasGetAncestors<OWLClass> ancestorsProvider,
                                @Nonnull PropertyValueMinimiser propertyValueMinimiser,
                                @Nonnull PropertyValueComparator propertyValueComparator,
                                @Nonnull Provider<AxiomPropertyValueTranslator> axiomPropertyValueTranslatorProvider) {
        this.ren = checkNotNull(renderer);
        this.rootOntology = checkNotNull(rootOntology);
        this.dataFactory = checkNotNull(dataFactory);
        this.ancestorsProvider = checkNotNull(ancestorsProvider);
        this.propertyValueMinimiser = checkNotNull(propertyValueMinimiser);
        this.propertyValueComparator = checkNotNull(propertyValueComparator);
        this.axiomPropertyValueTranslatorProvider = checkNotNull(axiomPropertyValueTranslatorProvider);
    }

    /**
     * Gets the entity type that this translator translates.
     *
     * @return The entity type.  Not {@code null}.
     */
    @Override
    public EntityType<OWLClass> getEntityType() {
        return EntityType.CLASS;
    }

    @Nonnull
    @Override
    public ClassFrame getFrame(@Nonnull OWLClassData subject) {
        return translateToClassFrame(subject);
    }

    @Nonnull
    @Override
    public Set<OWLAxiom> getAxioms(@Nonnull ClassFrame frame, @Nonnull Mode mode) {
        return translateToAxioms(frame.getSubject(), frame, mode);
    }

    private Set<OWLAxiom> translateToAxioms(OWLClassData subject, ClassFrame classFrame, Mode mode) {
        var result = new HashSet<OWLAxiom>();
        for (OWLClassData cls : classFrame.getClassEntries()) {
            result.add(dataFactory.getOWLSubClassOfAxiom(subject.getEntity(), cls.getEntity()));
        }
        for (PropertyValue propertyValue : classFrame.getPropertyValues()) {
            AxiomPropertyValueTranslator translator = axiomPropertyValueTranslatorProvider.get();
            result.addAll(translator.getAxioms(subject.getEntity(), propertyValue, mode));
        }
        return result;
    }

    private ClassFrame translateToClassFrame(OWLClassData subject) {
        var subjectData = ren.getClassData(subject.getEntity());
        var relevantAxioms = getRelevantAxioms(subject.getEntity(), true);
        var propertyValues = new ArrayList<>(translateAxiomsToPropertyValues(subject.getEntity(),
                                                                             relevantAxioms,
                                                                             State.ASSERTED));
        if (includeAncestorFrames) {
            for (OWLClass ancestor : ancestorsProvider.getAncestors(subject.getEntity())) {
                if (!ancestor.equals(subject.getEntity())) {
                    propertyValues.addAll(translateAxiomsToPropertyValues(ancestor,
                                                                          getRelevantAxioms(ancestor, false),
                                                                          State.DERIVED));
                }
            }
        }
        ImmutableSet<OWLClassData> entries = rootOntology.getSubClassAxiomsForSubClass(subject.getEntity()).stream()
                .filter(ax -> !ax.getSuperClass().isAnonymous())
                .map(ax -> ax.getSuperClass().asOWLClass())
                .distinct()
                .map(ren::getClassData)
                .collect(toImmutableSet());
        var propertyValuesMin = propertyValueMinimiser.minimisePropertyValues(propertyValues);
        propertyValuesMin.sort(propertyValueComparator);
        return ClassFrame.get(subjectData,
                              entries,
                              ImmutableSet.copyOf(propertyValuesMin));
    }

    private Set<OWLAxiom> getRelevantAxioms(OWLClass subject,
                                            boolean includeAnnotations) {
        var relevantAxioms = new HashSet<OWLAxiom>();
        for (OWLOntology ont : rootOntology.getImportsClosure()) {
            relevantAxioms.addAll(ont.getSubClassAxiomsForSubClass(subject));
            relevantAxioms.addAll(rootOntology.getEquivalentClassesAxioms(subject));
            if (includeAnnotations) {
                relevantAxioms.addAll(ont.getAnnotationAssertionAxioms(subject.getIRI()));
            }
        }
        return relevantAxioms;
    }

    private List<PropertyValue> translateAxiomsToPropertyValues(OWLClass subject,
                                                                Set<OWLAxiom> relevantAxioms,
                                                                State initialState) {
        var propertyValues = new ArrayList<PropertyValue>();
        for (OWLAxiom axiom : relevantAxioms) {
            AxiomPropertyValueTranslator translator = axiomPropertyValueTranslatorProvider.get();
            propertyValues.addAll(translator.getPropertyValues(subject, axiom, rootOntology, initialState));
        }
        return propertyValues;
    }
}
