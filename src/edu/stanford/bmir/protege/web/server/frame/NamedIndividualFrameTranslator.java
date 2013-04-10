package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.frame.NamedIndividualFrame;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValue;
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
    public NamedIndividualFrame getFrame(OWLNamedIndividual subject, OWLOntology rootOntology, OWLAPIProject project) {
        return translateToNamedIndividualFrame(subject, rootOntology, project);
    }

    @Override
    public Set<OWLAxiom> getAxioms(NamedIndividualFrame frame) {
        return translateToAxioms(frame.getSubject(), frame);
    }

    private NamedIndividualFrame translateToNamedIndividualFrame(OWLNamedIndividual subject, OWLOntology rootOntology, OWLAPIProject project) {
        Set<OWLAxiom> translateFrom = new HashSet<OWLAxiom>();
        for (OWLOntology ontology : rootOntology.getImportsClosure()) {
            translateFrom.addAll(ontology.getClassAssertionAxioms(subject));
            translateFrom.addAll(ontology.getAnnotationAssertionAxioms(subject.getIRI()));
            translateFrom.addAll(ontology.getObjectPropertyAssertionAxioms(subject));
            translateFrom.addAll(ontology.getDataPropertyAssertionAxioms(subject));
        }
        NamedIndividualFrame.Builder builder = new NamedIndividualFrame.Builder(subject);
        for(OWLAxiom axiom : translateFrom) {
            if(axiom instanceof OWLClassAssertionAxiom) {
                OWLClassAssertionAxiom ax = (OWLClassAssertionAxiom) axiom;
                if(ax.getIndividual().equals(subject) && !ax.getClassExpression().isAnonymous()) {
                    builder.addClass(ax.getClassExpression().asOWLClass());
                }
            }
        }
        List<PropertyValue> propertyValues = new ArrayList<PropertyValue>();
        for(OWLAxiom axiom : translateFrom) {
            AxiomPropertyValueTranslator translator = new AxiomPropertyValueTranslator();
            propertyValues.addAll(translator.getPropertyValues(subject, axiom, rootOntology));
        }
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


    private Set<OWLAxiom> translateToAxioms(OWLNamedIndividual subject, NamedIndividualFrame frame) {
        Set<OWLAxiom> result = new HashSet<OWLAxiom>();
        for(OWLClass cls : frame.getClasses()) {
            result.add(DataFactory.get().getOWLClassAssertionAxiom(cls, subject));
        }
        for(PropertyValue propertyValue : frame.getPropertyValues()) {
            AxiomPropertyValueTranslator translator = new AxiomPropertyValueTranslator();
            result.addAll(translator.getAxioms(subject, propertyValue));
        }
        for(OWLNamedIndividual individual : frame.getSameIndividuals()) {
            result.add(DataFactory.get().getOWLSameIndividualAxiom(subject, individual));
        }
        return result;
    }
}
