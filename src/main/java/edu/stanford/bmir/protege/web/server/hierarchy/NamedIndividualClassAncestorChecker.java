package edu.stanford.bmir.protege.web.server.hierarchy;

import org.semanticweb.owlapi.model.*;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 26/02/2014
 */
public class NamedIndividualClassAncestorChecker implements HasHasAncestor<OWLNamedIndividual, OWLClass> {

    private HasHasAncestor<OWLClass, OWLClass> classAncestorChecker;

    private OWLOntology ontology;

    public NamedIndividualClassAncestorChecker(OWLOntology ontology,
                                               HasHasAncestor<OWLClass, OWLClass> classAncestorChecker) {
        this.ontology = ontology;
        this.classAncestorChecker = classAncestorChecker;
    }

    @Override
    public boolean hasAncestor(OWLNamedIndividual ind, OWLClass cls) {
        for(OWLOntology ont : ontology.getImportsClosure()) {
            for(OWLClassAssertionAxiom ax : ont.getClassAssertionAxioms(ind)) {
                OWLClassExpression type = ax.getClassExpression();
                if (!type.isAnonymous()) {
                    if(type.equals(cls) || classAncestorChecker.hasAncestor(type.asOWLClass(), cls)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
