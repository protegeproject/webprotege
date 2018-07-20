package edu.stanford.bmir.protege.web.server.frame;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import edu.stanford.bmir.protege.web.server.hierarchy.HasGetAncestors;
import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.DataFactory;
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

import static com.google.common.collect.ImmutableList.toImmutableList;

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

    private boolean includeAncestorFrames = false;

    @Nonnull
    private final RenderingManager rm;

    @Nonnull
    private final OWLOntology rootOntology;

    @Nonnull
    private final HasGetAncestors<OWLClass> ancestorsProvider;

    @Nonnull
    private final PropertyValueMinimiser propertyValueMinimiser;

    @Nonnull
    private final PropertyValueComparator propertyValueComparator;

    @Nonnull
    private final Provider<AxiomPropertyValueTranslator> axiomPropertyValueTranslatorProvider;

    @Inject
    public ClassFrameTranslator(@Nonnull RenderingManager rm,
                                @Nonnull @RootOntology OWLOntology rootOntology,
                                @Nonnull HasGetAncestors<OWLClass> ancestorsProvider,
                                @Nonnull PropertyValueMinimiser propertyValueMinimiser,
                                @Nonnull PropertyValueComparator propertyValueComparator, @Nonnull Provider<AxiomPropertyValueTranslator> axiomPropertyValueTranslatorProvider) {
        this.rm = rm;
        this.rootOntology = rootOntology;
        this.ancestorsProvider = ancestorsProvider;
        this.propertyValueMinimiser = propertyValueMinimiser;
        this.propertyValueComparator = propertyValueComparator;
        this.axiomPropertyValueTranslatorProvider = axiomPropertyValueTranslatorProvider;
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

    @Override
    public ClassFrame getFrame(OWLClassData subject) {
        return translateToClassFrame(subject);
    }

    @Override
    public Set<OWLAxiom> getAxioms(ClassFrame frame, Mode mode) {
        return translateToAxioms(frame.getSubject(), frame, mode);
    }

    private ClassFrame translateToClassFrame(OWLClassData subject) {
        ClassFrame.Builder builder = ClassFrame.builder(rm.getRendering(subject.getEntity()));
        List<PropertyValue> propertyValues = Lists.newArrayList();
        final Set<OWLAxiom> relevantAxioms = getRelevantAxioms(subject.getEntity(), rootOntology, true);
        propertyValues.addAll(translateAxiomsToPropertyValues(subject.getEntity(),
                                                              rootOntology,
                                                              relevantAxioms,
                                                              State.ASSERTED));
        if (includeAncestorFrames) {
            for (OWLClass ancestor : ancestorsProvider.getAncestors(subject.getEntity())) {
                if (!ancestor.equals(subject.getEntity())) {
                    propertyValues.addAll(translateAxiomsToPropertyValues(ancestor,
                                                                          rootOntology,
                                                                          getRelevantAxioms(ancestor,
                                                                                            rootOntology, false),
                                                                          State.DERIVED));
                }
            }
        }
        propertyValues = propertyValueMinimiser.minimisePropertyValues(propertyValues);
        propertyValues.sort(propertyValueComparator);
        builder.setPropertyValues(propertyValues);
        ImmutableList<OWLClassData> entries = rootOntology.getSubClassAxiomsForSubClass(subject.getEntity()).stream()
                                                          .filter(ax -> !ax.getSuperClass().isAnonymous())
                                                          .map(ax -> ax.getSuperClass().asOWLClass())
                                                          .distinct()
                                                          .map(rm::getRendering)
                                                          .collect(toImmutableList());
        builder.setClassEntries(entries);
        return builder.build();
    }

    private List<PropertyValue> translateAxiomsToPropertyValues(OWLClass subject,
                                                                OWLOntology rootOntology,
                                                                Set<OWLAxiom> relevantAxioms,
                                                                State initialState) {
        List<PropertyValue> propertyValues = new ArrayList<>();
        for (OWLAxiom axiom : relevantAxioms) {
            AxiomPropertyValueTranslator translator = axiomPropertyValueTranslatorProvider.get();
            propertyValues.addAll(translator.getPropertyValues(subject, axiom, rootOntology, initialState));
        }
        return propertyValues;
    }

    private Set<OWLAxiom> getRelevantAxioms(OWLClass subject,
                                            OWLOntology rootOntology,
                                            boolean includeAnnotations) {
        final Set<OWLAxiom> relevantAxioms = new HashSet<>();
        for (OWLOntology ont : rootOntology.getImportsClosure()) {
            relevantAxioms.addAll(ont.getSubClassAxiomsForSubClass(subject));
            relevantAxioms.addAll(rootOntology.getEquivalentClassesAxioms(subject));
            if (includeAnnotations) {
                relevantAxioms.addAll(ont.getAnnotationAssertionAxioms(subject.getIRI()));
            }
        }
        return relevantAxioms;
    }

    private Set<OWLAxiom> translateToAxioms(OWLClassData subject, ClassFrame classFrame, Mode mode) {
        Set<OWLAxiom> result = new HashSet<>();
        for (OWLClassData cls : classFrame.getClassEntries()) {
            result.add(DataFactory.get().getOWLSubClassOfAxiom(subject.getEntity(), cls.getEntity()));
        }
        for (PropertyValue propertyValue : classFrame.getPropertyValues()) {
            AxiomPropertyValueTranslator translator = axiomPropertyValueTranslatorProvider.get();
            result.addAll(translator.getAxioms(subject.getEntity(), propertyValue, mode));
        }
        return result;
    }
}
