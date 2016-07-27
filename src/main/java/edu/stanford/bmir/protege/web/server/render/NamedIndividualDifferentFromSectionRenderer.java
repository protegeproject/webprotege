package edu.stanford.bmir.protege.web.server.render;

import com.google.common.collect.Lists;
import org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax;
import org.semanticweb.owlapi.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;

import java.util.List;
import java.util.Set;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 24/02/2014
 */
public class NamedIndividualDifferentFromSectionRenderer extends AbstractOWLAxiomItemSectionRenderer<OWLNamedIndividual, OWLDifferentIndividualsAxiom, OWLIndividual> {

    @Override
    public ManchesterOWLSyntax getSection() {
        return ManchesterOWLSyntax.DIFFERENT_FROM;
    }

    @Override
    protected Set<OWLDifferentIndividualsAxiom> getAxiomsInOntology(OWLNamedIndividual subject, OWLOntology ontology) {
        return ontology.getDifferentIndividualAxioms(subject);
    }

    @Override
    public List<OWLIndividual> getRenderablesForItem(OWLNamedIndividual subject,
                                                     OWLDifferentIndividualsAxiom item,
                                                     OWLOntology ontology) {
        Set<OWLIndividual> setResult = item.getIndividuals();
        setResult.remove(subject);
        return Lists.newArrayList(setResult);
    }
}
