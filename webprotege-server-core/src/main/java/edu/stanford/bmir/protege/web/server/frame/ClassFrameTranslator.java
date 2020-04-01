package edu.stanford.bmir.protege.web.server.frame;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.hierarchy.HasGetAncestors;
import edu.stanford.bmir.protege.web.server.index.AnnotationAssertionAxiomsBySubjectIndex;
import edu.stanford.bmir.protege.web.server.index.EquivalentClassesAxiomsIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import edu.stanford.bmir.protege.web.server.index.SubClassOfAxiomsBySubClassIndex;
import edu.stanford.bmir.protege.web.shared.frame.*;
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
public class ClassFrameTranslator {

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
    private final AxiomPropertyValueTranslator axiomPropertyValueTranslator;

    private boolean includeAncestorFrames = false;

    private boolean minimizePropertyValues = false;

    @Inject
    public ClassFrameTranslator(@Nonnull ProjectOntologiesIndex ontologiesIndex,
                                @Nonnull SubClassOfAxiomsBySubClassIndex subClassOfAxiomsIndex,
                                @Nonnull EquivalentClassesAxiomsIndex equivalentClassesAxiomsIndex,
                                @Nonnull AnnotationAssertionAxiomsBySubjectIndex annotationAssertionAxiomsIndex,
                                @Nonnull OWLDataFactory dataFactory,
                                @Nonnull HasGetAncestors<OWLClass> ancestorsProvider,
                                @Nonnull PropertyValueMinimiser propertyValueMinimiser,
                                @Nonnull AxiomPropertyValueTranslator axiomPropertyValueTranslator) {
        this.ontologiesIndex = ontologiesIndex;
        this.subClassOfAxiomsIndex = checkNotNull(subClassOfAxiomsIndex);
        this.equivalentClassesAxiomsIndex = checkNotNull(equivalentClassesAxiomsIndex);
        this.annotationAssertionAxiomsIndex = checkNotNull(annotationAssertionAxiomsIndex);
        this.dataFactory = checkNotNull(dataFactory);
        this.ancestorsProvider = checkNotNull(ancestorsProvider);
        this.propertyValueMinimiser = checkNotNull(propertyValueMinimiser);
        this.axiomPropertyValueTranslator = checkNotNull(axiomPropertyValueTranslator);
    }

    public boolean isIncludeAncestorFrames() {
        return includeAncestorFrames;
    }

    public void setIncludeAncestorFrames(boolean includeAncestorFrames) {
        this.includeAncestorFrames = includeAncestorFrames;
    }

    public void setMinimizePropertyValues(boolean minimizePropertyValues) {
        this.minimizePropertyValues = minimizePropertyValues;
    }

    @Nonnull
    public PlainClassFrame getFrame(@Nonnull OWLClass subject) {
        return translateToClassFrame(subject);
    }

    @Nonnull
    public Set<OWLAxiom> getAxioms(@Nonnull PlainClassFrame frame, @Nonnull Mode mode) {
        return translateToAxioms(frame.getSubject(), frame, mode);
    }

    private Set<OWLAxiom> translateToAxioms(OWLClass subject, PlainClassFrame classFrame, Mode mode) {
        var result = new HashSet<OWLAxiom>();
        for (OWLClass parent : classFrame.getParents()) {
            result.add(dataFactory.getOWLSubClassOfAxiom(subject, parent));
        }
        for (PlainPropertyValue propertyValue : classFrame.getPropertyValues()) {
            result.addAll(axiomPropertyValueTranslator.getAxioms(subject, propertyValue, mode));
        }
        return result;
    }

    private PlainClassFrame translateToClassFrame(OWLClass cls) {
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
                .collect(toImmutableSet());

        var propertyValuesMin = ImmutableSet.copyOf(propertyValues);


        if(minimizePropertyValues) {
            propertyValuesMin = propertyValueMinimiser.minimisePropertyValues(propertyValues)
                                .collect(toImmutableSet());
        }

        return PlainClassFrame.get(cls,
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

    private List<PlainPropertyValue> translateAxiomsToPropertyValues(OWLClass subject,
                                                                     Set<OWLAxiom> relevantAxioms,
                                                                     State initialState) {
        return relevantAxioms.stream()
                .flatMap(axiom -> axiomPropertyValueTranslator.getPropertyValues(subject, axiom, initialState).stream())
                .collect(Collectors.toList());
    }
}
