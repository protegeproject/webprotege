package edu.stanford.bmir.protege.web.server.frame;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import edu.stanford.bmir.protege.web.server.hierarchy.ClassClassAncestorChecker;
import edu.stanford.bmir.protege.web.server.hierarchy.DataPropertyDataPropertyAncestorChecker;
import edu.stanford.bmir.protege.web.server.hierarchy.NamedIndividualClassAncestorChecker;
import edu.stanford.bmir.protege.web.server.hierarchy.ObjectPropertyObjectPropertyAncestorChecker;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.frame.ClassFrame;
import edu.stanford.bmir.protege.web.shared.frame.ClassFrameType;
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
public class ClassFrameTranslator implements EntityFrameTranslator<ClassFrame, OWLClass> {

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
    public ClassFrame getFrame(OWLClass subject, OWLOntology rootOntology, OWLAPIProject project) {
        return translateToClassFrame(subject, rootOntology, project);
    }

    @Override
    public Set<OWLAxiom> getAxioms(ClassFrame frame, Mode mode) {
        return translateToAxioms(frame.getSubject(), frame, mode);
    }

    private ClassFrame translateToClassFrame(OWLClass subject, OWLOntology rootOntology, final OWLAPIProject project) {
        ClassFrame.Builder builder = new ClassFrame.Builder(subject);
        List<PropertyValue> propertyValues = Lists.newArrayList();
        final Set<OWLAxiom> relevantAxioms = getRelevantAxioms(subject, rootOntology, builder, true);
        propertyValues.addAll(translateAxiomsToPropertyValues(subject,
                rootOntology,
                relevantAxioms,
                PropertyValueState.ASSERTED));
        for (OWLClass ancestor : project.getClassHierarchyProvider().getAncestors(subject)) {
            if (!ancestor.equals(subject)) {
                propertyValues.addAll(translateAxiomsToPropertyValues(ancestor,
                        rootOntology,
                        getRelevantAxioms(ancestor, rootOntology, builder, false),
                        PropertyValueState.DERIVED));
            }
        }
        builder.setClassFrameType(ClassFrameType.ASSERTED);
        if (propertyValues.isEmpty()) {
            builder.setClassFrameType(ClassFrameType.ASSERTED);
        }
        propertyValues = new PropertyValueMinimiser().minimisePropertyValues(propertyValues, rootOntology, project);
        Collections.sort(propertyValues, new PropertyValueComparator(project));
        builder.addPropertyValues(propertyValues);
        return builder.build();
    }

//    private List<PropertyValue> prunePropertyValues(List<PropertyValue> propertyValues,
//                                                    OWLOntology ontology,
//                                                    OWLAPIProject project) {
//        List<PropertyValue> result = Lists.newArrayList(propertyValues);
//        for (int i = 0; i < propertyValues.size(); i++) {
//            for (int j = 0; j < propertyValues.size(); j++) {
//                if (i != j && result.get(i) != null && result.get(j) != null) {
//                    PropertyValue propertyValueA = propertyValues.get(i);
//                    PropertyValue propertyValueB = propertyValues.get(j);
//                    PropertyValueSubsumptionChecker subsumptionChecker = getPropertyValueSubsumptionChecler(ontology,
//                            project);
//                    if (subsumptionChecker.isSubsumedBy(propertyValueA, propertyValueB)) {
//                        // Don't show B because this is more specific!
//                        result.set(j, null);
//                    }
//                }
//            }
//        }
//        for (Iterator<PropertyValue> it = result.iterator(); it.hasNext(); ) {
//            if (it.next() == null) {
//                it.remove();
//            }
//        }
//        return result;
//    }

//    private PropertyValueSubsumptionChecker getPropertyValueSubsumptionChecler(OWLOntology ontology,
//                                                                               OWLAPIProject project) {
//        ClassClassAncestorChecker classAncestorChecker = new ClassClassAncestorChecker(project
//                .getClassHierarchyProvider());
//        ObjectPropertyObjectPropertyAncestorChecker objectPropertyAncestorChecker = new
//                ObjectPropertyObjectPropertyAncestorChecker(
//                project.getObjectPropertyHierarchyProvider());
//        DataPropertyDataPropertyAncestorChecker dataPropertyAncestorChecker = new
//                DataPropertyDataPropertyAncestorChecker(
//                project.getDataPropertyHierarchyProvider());
//        NamedIndividualClassAncestorChecker namedIndividualClassAncestorChecker = new
//                NamedIndividualClassAncestorChecker(
//                ontology,
//                classAncestorChecker);
//        return new StructuralPropertyValueSubsumptionChecker(classAncestorChecker,
//                objectPropertyAncestorChecker,
//                dataPropertyAncestorChecker,
//                namedIndividualClassAncestorChecker);
//    }

    private List<PropertyValue> translateAxiomsToPropertyValues(OWLClass subject,
                                                                OWLOntology rootOntology,
                                                                Set<OWLAxiom> relevantAxioms,
                                                                PropertyValueState initialState) {
        List<PropertyValue> propertyValues = new ArrayList<PropertyValue>();
        for (OWLAxiom axiom : relevantAxioms) {
            AxiomPropertyValueTranslator translator = new AxiomPropertyValueTranslator();
            propertyValues.addAll(translator.getPropertyValues(subject, axiom, rootOntology, initialState));
        }
        return propertyValues;
    }

    private Set<OWLAxiom> getRelevantAxioms(OWLClass subject,
                                            OWLOntology rootOntology,
                                            ClassFrame.Builder builder,
                                            boolean includeAnnotations) {
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

    private Set<OWLAxiom> translateToAxioms(OWLClass subject, ClassFrame classFrame, Mode mode) {
        Set<OWLAxiom> result = new HashSet<OWLAxiom>();
//        for (OWLClass cls : classFrame.getClasses()) {
//            result.add(DataFactory.get().getOWLSubClassOfAxiom(subject, cls));
//        }
        for (PropertyValue propertyValue : classFrame.getPropertyValues()) {
            AxiomPropertyValueTranslator translator = new AxiomPropertyValueTranslator();
            result.addAll(translator.getAxioms(subject, propertyValue, mode));
        }
        return result;
    }
}
