package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.frame.ClassFrame;
import edu.stanford.bmir.protege.web.shared.frame.ClassFrameType;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValue;
import org.semanticweb.owlapi.model.*;

import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/12/2012
 * <p>
 *     A translator that converts sets of axioms to class frames and vice-versa.
 * </p>
 */
public class ClassFrameTranslator implements EntityFrameTranslator<ClassFrame, OWLClass> {

    /**
     * Gets the entity type that this translator translates.
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
        final Set<OWLAxiom> relevantAxioms = new HashSet<OWLAxiom>();

        for(OWLOntology ont : rootOntology.getImportsClosure()) {
            for(OWLSubClassOfAxiom subClassOfAxiom : ont.getSubClassAxiomsForSubClass(subject)) {
                if(!subClassOfAxiom.getSuperClass().isAnonymous()) {
                    builder.addClass(subClassOfAxiom.getSuperClass().asOWLClass());
                }
                else {
                    relevantAxioms.add(subClassOfAxiom);
                }
            }
            // TODO: Needs some more thought
//            if (relevantAxioms.isEmpty()) {
//                for(OWLEquivalentClassesAxiom equivalentClassesAxiom : rootOntology.getEquivalentClassesAxioms(subject)) {
//                    for(OWLSubClassOfAxiom sca : equivalentClassesAxiom.asOWLSubClassOfAxioms()) {
//                        Set<OWLClassExpression> supers = sca.getSuperClass().asConjunctSet();
//                        for(OWLClassExpression sup : supers) {
//                            if (sca.getSubClass().equals(subject) && sup.isAnonymous()) {
//                                relevantAxioms.add(project.getDataFactory().getOWLSubClassOfAxiom(sca.getSubClass(), sup));
//                            }
//                        }
//                    }
//                }
//                if(!relevantAxioms.isEmpty()) {
//                    builder.setClassFrameType(ClassFrameType.DERIVED);
//                }
//            }
//            else {
                builder.setClassFrameType(ClassFrameType.ASSERTED);
//            }
            relevantAxioms.addAll(ont.getAnnotationAssertionAxioms(subject.getIRI()));
        }
        List<PropertyValue> propertyValues = new ArrayList<PropertyValue>();
        for(OWLAxiom axiom : relevantAxioms) {
            AxiomPropertyValueTranslator translator = new AxiomPropertyValueTranslator();
            propertyValues.addAll(translator.getPropertyValues(subject, axiom, rootOntology));
        }
        if(propertyValues.isEmpty()) {
            builder.setClassFrameType(ClassFrameType.ASSERTED);
        }
        Collections.sort(propertyValues, new PropertyValueComparator(project));
        builder.addPropertyValues(propertyValues);
        return builder.build();
    }


    private Set<OWLAxiom> translateToAxioms(OWLClass subject, ClassFrame classFrame,  Mode mode) {
        Set<OWLAxiom> result = new HashSet<OWLAxiom>();
        for(OWLClass cls : classFrame.getClasses()) {
            result.add(DataFactory.get().getOWLSubClassOfAxiom(subject, cls));
        }
        for(PropertyValue propertyValue : classFrame.getPropertyValues()) {
            AxiomPropertyValueTranslator translator = new AxiomPropertyValueTranslator();
            result.addAll(translator.getAxioms(subject, propertyValue, mode));

        }
        return result;
    }



}
