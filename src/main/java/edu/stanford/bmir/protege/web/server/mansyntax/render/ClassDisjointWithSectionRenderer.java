package edu.stanford.bmir.protege.web.server.mansyntax.render;

import org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLOntology;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 24/02/2014
 */
public class ClassDisjointWithSectionRenderer extends AbstractOWLAxiomItemSectionRenderer<OWLClass, OWLDisjointClassesAxiom, OWLClassExpression> {


    public static final String ELEMENT_SEPARATOR = " | ";

    @Override
    public ManchesterOWLSyntax getSection() {
        return ManchesterOWLSyntax.DISJOINT_WITH;
    }

    @Override
    protected Set<OWLDisjointClassesAxiom> getAxiomsInOntology(OWLClass subject, OWLOntology ontology) {
        return ontology.getDisjointClassesAxioms(subject);
    }
    @Override
    public List<OWLClassExpression> getRenderablesForItem(OWLClass subject,
                                                          OWLDisjointClassesAxiom item,
                                                          OWLOntology ontology) {
        return new ArrayList<OWLClassExpression>(item.getClassExpressionsMinus(subject));
    }

}
