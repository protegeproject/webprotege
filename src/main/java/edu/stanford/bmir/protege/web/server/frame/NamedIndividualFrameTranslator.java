package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.frame.ClassFrame;
import edu.stanford.bmir.protege.web.shared.frame.NamedIndividualFrame;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValue;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValueState;
import org.semanticweb.owlapi.model.*;

import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/12/2012
 */
public class NamedIndividualFrameTranslator implements EntityFrameTranslator<NamedIndividualFrame, OWLNamedIndividual> {

    /**
     * Gets the entity type that this translator translates.
     * @return The entity type.  Not {@code null}.
     */
    @Override
    public EntityType<OWLNamedIndividual> getEntityType() {
        return EntityType.NAMED_INDIVIDUAL;
    }

    @Override
    public NamedIndividualFrame getFrame(OWLNamedIndividual subject, OWLOntology rootOntology, Project project) {
        return translateToNamedIndividualFrame(subject, rootOntology, project);
    }

    @Override
    public Set<OWLAxiom> getAxioms(NamedIndividualFrame frame, Mode mode) {
        return translateToAxioms(frame.getSubject(), frame, mode);
    }

    private NamedIndividualFrame translateToNamedIndividualFrame(OWLNamedIndividual subject, OWLOntology rootOntology, Project project) {
        Set<OWLAxiom> relevantAxioms = getRelevantAxioms(subject, rootOntology);

        NamedIndividualFrame.Builder builder = new NamedIndividualFrame.Builder(subject);
        for(OWLAxiom axiom : relevantAxioms) {
            if(axiom instanceof OWLClassAssertionAxiom) {
                OWLClassAssertionAxiom ax = (OWLClassAssertionAxiom) axiom;
                if(ax.getIndividual().equals(subject) && !ax.getClassExpression().isAnonymous()) {
                    builder.addClass(ax.getClassExpression().asOWLClass());
                }
            }
        }
        List<PropertyValue> propertyValues = new ArrayList<PropertyValue>();
        for(OWLAxiom axiom : relevantAxioms) {
            AxiomPropertyValueTranslator translator = new AxiomPropertyValueTranslator();
            propertyValues.addAll(translator.getPropertyValues(subject, axiom, rootOntology, PropertyValueState.ASSERTED));
        }
        for(OWLOntology ont : rootOntology.getImportsClosure()) {
            for(OWLClassAssertionAxiom ax : ont.getClassAssertionAxioms(subject)) {
                if(!ax.getClassExpression().isAnonymous()) {
                    OWLClass type = (OWLClass) ax.getClassExpression();
                    ClassFrameTranslator classFrameTranslator = new ClassFrameTranslator();
                    ClassFrame classFrame = classFrameTranslator.getFrame(type, rootOntology, project);
                    for(PropertyValue propertyValue : classFrame.getPropertyValues()) {
                        // Bit yucky
                        if (!propertyValue.isAnnotation()) {
                            propertyValues.add(propertyValue.setState(PropertyValueState.DERIVED));
                        }
                    }
                }
            }
        }

        propertyValues = new PropertyValueMinimiser().minimisePropertyValues(propertyValues, rootOntology, project);
        Collections.sort(propertyValues, new PropertyValueComparator(project));
        builder.addPropertyValues(propertyValues);
        for (OWLOntology ont : rootOntology.getImportsClosure()) {
            for(OWLSameIndividualAxiom sameIndividualAxiom : ont.getSameIndividualAxioms(subject)) {
                final Set<OWLIndividual> individuals = sameIndividualAxiom.getIndividuals();
                for(OWLIndividual ind : individuals) {
                    if(!ind.isAnonymous() && !ind.equals(subject)) {
                        builder.addSameIndividual(ind.asOWLNamedIndividual());
                    }
                }
            }
        }

        return builder.build();
    }

    private Set<OWLAxiom> getRelevantAxioms(OWLNamedIndividual subject, OWLOntology rootOntology) {
        Set<OWLAxiom> relevantAxioms = new HashSet<OWLAxiom>();
        for (OWLOntology ontology : rootOntology.getImportsClosure()) {
            relevantAxioms.addAll(ontology.getClassAssertionAxioms(subject));
            relevantAxioms.addAll(ontology.getAnnotationAssertionAxioms(subject.getIRI()));
            relevantAxioms.addAll(ontology.getObjectPropertyAssertionAxioms(subject));
            relevantAxioms.addAll(ontology.getDataPropertyAssertionAxioms(subject));
        }
        return relevantAxioms;
    }

    private Set<OWLAxiom> translateToAxioms(OWLNamedIndividual subject, NamedIndividualFrame frame, Mode mode) {
        Set<OWLAxiom> result = new HashSet<OWLAxiom>();
        for(OWLClass cls : frame.getClasses()) {
            result.add(DataFactory.get().getOWLClassAssertionAxiom(cls, subject));
        }
        for(PropertyValue propertyValue : frame.getPropertyValues()) {
            AxiomPropertyValueTranslator translator = new AxiomPropertyValueTranslator();
            result.addAll(translator.getAxioms(subject, propertyValue, mode));
        }
        for(OWLNamedIndividual individual : frame.getSameIndividuals()) {
            result.add(DataFactory.get().getOWLSameIndividualAxiom(subject, individual));
        }
        return result;
    }
}
