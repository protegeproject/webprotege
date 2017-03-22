package edu.stanford.bmir.protege.web.server.frame;

import com.google.common.collect.Lists;
import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.frame.ClassFrame;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValue;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValueState;
import org.semanticweb.owlapi.model.*;

import java.util.*;

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
    public ClassFrame getFrame(OWLClassData subject, OWLOntology rootOntology, Project project) {
        return translateToClassFrame(subject, rootOntology, project);
    }

    @Override
    public Set<OWLAxiom> getAxioms(ClassFrame frame, Mode mode) {
        return translateToAxioms(frame.getSubject(), frame, mode);
    }

    private ClassFrame translateToClassFrame(OWLClassData subject, OWLOntology rootOntology, final Project project) {
        RenderingManager rm = project.getRenderingManager();
        ClassFrame.Builder builder = new ClassFrame.Builder(rm.getRendering(subject.getEntity()));
        List<PropertyValue> propertyValues = Lists.newArrayList();
        final Set<OWLAxiom> relevantAxioms = getRelevantAxioms(subject.getEntity(), rootOntology, builder, true, project);
        propertyValues.addAll(translateAxiomsToPropertyValues(subject.getEntity(),
                rootOntology,
                relevantAxioms,
                PropertyValueState.ASSERTED, project));
        for (OWLClass ancestor : project.getClassHierarchyProvider().getAncestors(subject.getEntity())) {
            if (!ancestor.equals(subject.getEntity())) {
                propertyValues.addAll(translateAxiomsToPropertyValues(ancestor,
                        rootOntology,
                        getRelevantAxioms(ancestor, rootOntology, builder, false, project),
                        PropertyValueState.DERIVED, project));
            }
        }
        propertyValues = new PropertyValueMinimiser().minimisePropertyValues(propertyValues, rootOntology, project);
        Collections.sort(propertyValues, new PropertyValueComparator(project));
        builder.addPropertyValues(propertyValues);
        for(OWLSubClassOfAxiom ax : rootOntology.getSubClassAxiomsForSubClass(subject.getEntity())) {
            if(!ax.getSuperClass().isAnonymous()) {
                builder.addClass(rm.getRendering(ax.getSuperClass().asOWLClass()));
            }
        }
        return builder.build();
    }

    private List<PropertyValue> translateAxiomsToPropertyValues(OWLClass subject,
                                                                OWLOntology rootOntology,
                                                                Set<OWLAxiom> relevantAxioms,
                                                                PropertyValueState initialState,
                                                                Project project) {
        List<PropertyValue> propertyValues = new ArrayList<PropertyValue>();
        for (OWLAxiom axiom : relevantAxioms) {
            AxiomPropertyValueTranslator translator = new AxiomPropertyValueTranslator();
            propertyValues.addAll(translator.getPropertyValues(subject, axiom, rootOntology, initialState, project.getRenderingManager()));
        }
        return propertyValues;
    }

    private Set<OWLAxiom> getRelevantAxioms(OWLClass subject,
                                            OWLOntology rootOntology,
                                            ClassFrame.Builder builder,
                                            boolean includeAnnotations,
                                            Project project) {
        final Set<OWLAxiom> relevantAxioms = new HashSet<OWLAxiom>();
        for (OWLOntology ont : rootOntology.getImportsClosure()) {
            for (OWLSubClassOfAxiom subClassOfAxiom : ont.getSubClassAxiomsForSubClass(subject)) {
                relevantAxioms.add(subClassOfAxiom);
            }
            for (OWLEquivalentClassesAxiom ax : rootOntology.getEquivalentClassesAxioms(subject)) {
                relevantAxioms.add(ax);
            }
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
            AxiomPropertyValueTranslator translator = new AxiomPropertyValueTranslator();
            result.addAll(translator.getAxioms(subject.getEntity(), propertyValue, mode));
        }
        return result;
    }
}
