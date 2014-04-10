package edu.stanford.bmir.protege.web.server.render;

import com.google.common.collect.Lists;
import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntax;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;

import java.util.List;
import java.util.Set;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 24/02/2014
 */
public class NamedIndividualTypesSectionRenderer extends AbstractOWLAxiomItemSectionRenderer<OWLNamedIndividual, OWLClassAssertionAxiom, OWLClassExpression> {

    @Override
    public ManchesterOWLSyntax getSection() {
        return ManchesterOWLSyntax.TYPES;
    }

    @Override
    protected Set<OWLClassAssertionAxiom> getAxiomsInOntology(OWLNamedIndividual subject, OWLOntology ontology) {
        return ontology.getClassAssertionAxioms(subject);
    }

    @Override
    public List<OWLClassExpression> getRenderablesForItem(OWLNamedIndividual subject,
                                                          OWLClassAssertionAxiom item,
                                                          OWLOntology ontology) {
        return Lists.newArrayList(item.getClassExpression());
    }
}
