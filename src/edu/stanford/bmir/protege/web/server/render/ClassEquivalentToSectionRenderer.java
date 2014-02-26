package edu.stanford.bmir.protege.web.server.render;

import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntax;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLOntology;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 24/02/2014
 */
public class ClassEquivalentToSectionRenderer extends AbstractOWLAxiomItemSectionRenderer<OWLClass, OWLEquivalentClassesAxiom, OWLClassExpression> {

    @Override
    public ManchesterOWLSyntax getSection() {
        return ManchesterOWLSyntax.EQUIVALENT_TO;
    }

    @Override
    protected Set<OWLEquivalentClassesAxiom> getAxiomsInOntology(OWLClass subject, OWLOntology ontology) {
        return ontology.getEquivalentClassesAxioms(subject);
    }

    @Override
    public List<OWLClassExpression> getRenderablesForItem(OWLClass subject,
                                                          OWLEquivalentClassesAxiom item,
                                                          OWLOntology ontology) {
        return new ArrayList<OWLClassExpression>(item.getClassExpressionsMinus(subject));
    }
}
