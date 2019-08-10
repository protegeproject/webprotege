package edu.stanford.bmir.protege.web.server.frame;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.hierarchy.HasGetAncestors;
import edu.stanford.bmir.protege.web.server.index.AnnotationAssertionAxiomsBySubjectIndex;
import edu.stanford.bmir.protege.web.server.index.EquivalentClassesAxiomsIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import edu.stanford.bmir.protege.web.server.index.SubClassOfAxiomsBySubClassIndex;
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
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Nonnull
    private final SubClassOfAxiomsBySubClassIndex subClassOfAxiomsBySubClassIndex;

    @Nonnull
    private final EquivalentClassesAxiomsIndex equivalentClassesAxiomsIndex;
    
    @Nonnull
    private final AnnotationAssertionAxiomsBySubjectIndex annotationAssertionAxiomsBySubjectIndex;

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
                                @Nonnull ProjectOntologiesIndex projectOntologiesIndex,
                                @Nonnull SubClassOfAxiomsBySubClassIndex subClassOfAxiomsBySubClassIndex,
                                @Nonnull EquivalentClassesAxiomsIndex equivalentClassesAxiomsIndex,
                                @Nonnull AnnotationAssertionAxiomsBySubjectIndex annotationAssertionAxiomsBySubjectIndex,
                                @Nonnull OWLDataFactory dataFactory,
                                @Nonnull HasGetAncestors<OWLClass> ancestorsProvider,
                                @Nonnull PropertyValueMinimiser propertyValueMinimiser,
                                @Nonnull PropertyValueComparator propertyValueComparator,
                                @Nonnull Provider<AxiomPropertyValueTranslator> axiomPropertyValueTranslatorProvider) {
        this.ren = checkNotNull(renderer);
        this.projectOntologiesIndex = projectOntologiesIndex;
        this.subClassOfAxiomsBySubClassIndex = checkNotNull(subClassOfAxiomsBySubClassIndex);
        this.equivalentClassesAxiomsIndex = checkNotNull(equivalentClassesAxiomsIndex);
        this.annotationAssertionAxiomsBySubjectIndex = checkNotNull(annotationAssertionAxiomsBySubjectIndex);
        this.dataFactory = checkNotNull(dataFactory);
        this.ancestorsProvider = checkNotNull(ancestorsProvider);
        this.propertyValueMinimiser = checkNotNull(propertyValueMinimiser);
        this.propertyValueComparator = checkNotNull(propertyValueComparator);
        this.axiomPropertyValueTranslatorProvider = checkNotNull(axiomPropertyValueTranslatorProvider);
    }

    public boolean isIncludeAncestorFrames() {
        return includeAncestorFrames;
    }

    public void setIncludeAncestorFrames(boolean includeAncestorFrames) {
        this.includeAncestorFrames = includeAncestorFrames;
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

        var parents = projectOntologiesIndex.getOntologyIds()
                .flatMap(ontId -> subClassOfAxiomsBySubClassIndex.getSubClassOfAxiomsForSubClass(subject.getEntity(), ontId))
                .map(OWLSubClassOfAxiom::getSuperClass)
                .filter(OWLClassExpression::isNamed)
                .map(OWLClassExpression::asOWLClass)
                .distinct()
                .map(ren::getClassData)
                .collect(toImmutableSet());

        var propertyValuesMin = propertyValueMinimiser.minimisePropertyValues(propertyValues);
        propertyValuesMin.sort(propertyValueComparator);
        return ClassFrame.get(subjectData,
                              parents,
                              ImmutableSet.copyOf(propertyValuesMin));
    }

    private Set<OWLAxiom> getRelevantAxioms(OWLClass subject,
                                            boolean includeAnnotations) {
        var relevantAxioms = new HashSet<OWLAxiom>();
        projectOntologiesIndex.getOntologyIds()
                .forEach(ont -> {
                    subClassOfAxiomsBySubClassIndex.getSubClassOfAxiomsForSubClass(subject, ont)
                            .forEach(relevantAxioms::add);
                    equivalentClassesAxiomsIndex.getEquivalentClassesAxioms(subject, ont)
                            .forEach(relevantAxioms::add);
                    if (includeAnnotations) {
                        annotationAssertionAxiomsBySubjectIndex.getAxiomsForSubject(subject.getIRI(), ont)
                                .forEach(relevantAxioms::add);
                    }
                });
        return relevantAxioms;
    }

    private List<PropertyValue> translateAxiomsToPropertyValues(OWLClass subject,
                                                                Set<OWLAxiom> relevantAxioms,
                                                                State initialState) {
        var propertyValues = new ArrayList<PropertyValue>();
        for (OWLAxiom axiom : relevantAxioms) {
            AxiomPropertyValueTranslator translator = axiomPropertyValueTranslatorProvider.get();
            propertyValues.addAll(translator.getPropertyValues(subject, axiom, initialState));
        }
        return propertyValues;
    }
}
