package edu.stanford.bmir.protege.web.server.hierarchy;

import edu.stanford.bmir.protege.web.server.index.ClassAssertionAxiomsByIndividualIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 26/02/2014
 */
public class NamedIndividualClassAncestorChecker implements HasHasAncestor<OWLNamedIndividual, OWLClass> {

    @Nonnull
    private HasHasAncestor<OWLClass, OWLClass> classAncestorChecker;

    @Nonnull
    private final ClassAssertionAxiomsByIndividualIndex axiomsIndex;

    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Inject
    public NamedIndividualClassAncestorChecker(@Nonnull HasHasAncestor<OWLClass, OWLClass> classAncestorChecker,
                                               @Nonnull ClassAssertionAxiomsByIndividualIndex axiomsIndex,
                                               @Nonnull ProjectOntologiesIndex projectOntologiesIndex) {
        this.axiomsIndex = axiomsIndex;
        this.classAncestorChecker = classAncestorChecker;
        this.projectOntologiesIndex = projectOntologiesIndex;
    }

    @Override
    public boolean hasAncestor(OWLNamedIndividual ind, OWLClass cls) {
        return projectOntologiesIndex.getOntologyIds()
                              .flatMap(ontId -> axiomsIndex.getClassAssertionAxioms(ind, ontId))
                              .map(OWLClassAssertionAxiom::getClassExpression)
                              .filter(OWLClassExpression::isNamed)
                              .map(OWLClassExpression::asOWLClass)
                              .anyMatch(ce -> ce.equals(cls) || classAncestorChecker.hasAncestor(ce, cls));
    }
}
