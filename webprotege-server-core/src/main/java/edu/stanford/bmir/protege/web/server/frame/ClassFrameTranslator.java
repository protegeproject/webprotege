package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.server.hierarchy.HasGetAncestors;
import edu.stanford.bmir.protege.web.server.index.AnnotationAssertionAxiomsBySubjectIndex;
import edu.stanford.bmir.protege.web.server.index.EquivalentClassesAxiomsIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import edu.stanford.bmir.protege.web.server.index.SubClassOfAxiomsBySubClassIndex;
import edu.stanford.bmir.protege.web.server.renderer.ContextRenderer;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.frame.ClassFrame;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValue;
import edu.stanford.bmir.protege.web.shared.frame.State;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private final ProjectOntologiesIndex ontologiesIndex;

    @Nonnull
    private final SubClassOfAxiomsBySubClassIndex subClassOfAxiomsIndex;

    @Nonnull
    private final EquivalentClassesAxiomsIndex equivalentClassesAxiomsIndex;
    
    @Nonnull
    private final AnnotationAssertionAxiomsBySubjectIndex annotationAssertionAxiomsIndex;

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Nonnull
    private final HasGetAncestors<OWLClass> ancestorsProvider;

    @Nonnull
    private final PropertyValueMinimiser propertyValueMinimiser;

    @Nonnull
    private final PropertyValueComparator propertyValueComparator;

    @Nonnull
    private final AxiomPropertyValueTranslator axiomPropertyValueTranslator;

    private boolean includeAncestorFrames = false;

    @Inject
    public ClassFrameTranslator(@Nonnull ContextRenderer renderer,
                                @Nonnull ProjectOntologiesIndex ontologiesIndex,
                                @Nonnull SubClassOfAxiomsBySubClassIndex subClassOfAxiomsIndex,
                                @Nonnull EquivalentClassesAxiomsIndex equivalentClassesAxiomsIndex,
                                @Nonnull AnnotationAssertionAxiomsBySubjectIndex annotationAssertionAxiomsIndex,
                                @Nonnull OWLDataFactory dataFactory,
                                @Nonnull HasGetAncestors<OWLClass> ancestorsProvider,
                                @Nonnull PropertyValueMinimiser propertyValueMinimiser,
                                @Nonnull PropertyValueComparator propertyValueComparator,
                                @Nonnull AxiomPropertyValueTranslator axiomPropertyValueTranslator) {
        this.ren = checkNotNull(renderer);
        this.ontologiesIndex = ontologiesIndex;
        this.subClassOfAxiomsIndex = checkNotNull(subClassOfAxiomsIndex);
        this.equivalentClassesAxiomsIndex = checkNotNull(equivalentClassesAxiomsIndex);
        this.annotationAssertionAxiomsIndex = checkNotNull(annotationAssertionAxiomsIndex);
        this.dataFactory = checkNotNull(dataFactory);
        this.ancestorsProvider = checkNotNull(ancestorsProvider);
        this.propertyValueMinimiser = checkNotNull(propertyValueMinimiser);
        this.propertyValueComparator = checkNotNull(propertyValueComparator);
        this.axiomPropertyValueTranslator = checkNotNull(axiomPropertyValueTranslator);
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
            result.addAll(axiomPropertyValueTranslator.getAxioms(subject.getEntity(), propertyValue, mode));
        }
        return result;
    }

    private ClassFrame translateToClassFrame(OWLClassData subject) {
        var cls = subject.getEntity();
        var subjectData = ren.getClassData(cls);
        var relevantAxioms = getRelevantAxioms(cls, true);
        var propertyValues = new ArrayList<>(translateAxiomsToPropertyValues(cls,
                                                                             relevantAxioms,
                                                                             State.ASSERTED));
        if (includeAncestorFrames) {
            for (OWLClass ancestor : ancestorsProvider.getAncestors(cls)) {
                if (!ancestor.equals(cls)) {
                    propertyValues.addAll(translateAxiomsToPropertyValues(ancestor,
                                                                          getRelevantAxioms(ancestor, false),
                                                                          State.DERIVED));
                }
            }
        }

        var parents = getSubClassOfAxioms(cls)
                .map(OWLSubClassOfAxiom::getSuperClass)
                .filter(OWLClassExpression::isNamed)
                .map(OWLClassExpression::asOWLClass)
                .distinct()
                .map(ren::getClassData)
                .collect(toImmutableSet());

        var propertyValuesMin = propertyValueMinimiser.minimisePropertyValues(propertyValues)
                .sorted(propertyValueComparator)
                .collect(toImmutableSet());

        return ClassFrame.get(subjectData,
                              parents,
                              propertyValuesMin);
    }

    private Set<OWLAxiom> getRelevantAxioms(OWLClass subject,
                                            boolean includeAnnotations) {
        var subClassOfAxioms = getSubClassOfAxioms(subject);
        var equivalentClassesAxioms = getEquivalentClassesAxioms(subject);
        var annotationAssertions = getAnnotationAssertions(subject, includeAnnotations);
        return Stream.of(subClassOfAxioms,
                         equivalentClassesAxioms,
                         annotationAssertions)
                .flatMap(ax -> ax)
                .collect(toImmutableSet());
    }

    private Stream<OWLSubClassOfAxiom> getSubClassOfAxioms(OWLClass subject) {
        return ontologiesIndex
                .getOntologyIds()
                .flatMap(ontId -> subClassOfAxiomsIndex.getSubClassOfAxiomsForSubClass(subject, ontId));
    }

    private Stream<OWLEquivalentClassesAxiom> getEquivalentClassesAxioms(OWLClass subject) {
        return ontologiesIndex.getOntologyIds()
        .flatMap(ontId -> equivalentClassesAxiomsIndex.getEquivalentClassesAxioms(subject, ontId));
    }

    private Stream<OWLAnnotationAssertionAxiom> getAnnotationAssertions(OWLClass subject, boolean includeAnnotations) {
        if(!includeAnnotations) {
            return Stream.empty();
        }
        return ontologiesIndex.getOntologyIds()
        .flatMap(ontId -> annotationAssertionAxiomsIndex.getAxiomsForSubject(subject.getIRI(), ontId));
    }

    private List<PropertyValue> translateAxiomsToPropertyValues(OWLClass subject,
                                                                Set<OWLAxiom> relevantAxioms,
                                                                State initialState) {
        return relevantAxioms.stream()
                .flatMap(axiom -> axiomPropertyValueTranslator.getPropertyValues(subject, axiom, initialState).stream())
                .collect(Collectors.toList());
    }
}
