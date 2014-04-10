package edu.stanford.bmir.protege.web.server.render;

import com.google.common.collect.Lists;
import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntax;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLSameIndividualAxiom;

import java.util.List;
import java.util.Set;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 24/02/2014
 */
public class NamedIndividualSameAsSectionRenderer extends AbstractOWLAxiomItemSectionRenderer<OWLNamedIndividual, OWLSameIndividualAxiom, OWLIndividual> {

    @Override
    public ManchesterOWLSyntax getSection() {
        return ManchesterOWLSyntax.SAME_AS;
    }

    @Override
    protected Set<OWLSameIndividualAxiom> getAxiomsInOntology(OWLNamedIndividual subject, OWLOntology ontology) {
        return ontology.getSameIndividualAxioms(subject);
    }

    @Override
    public List<OWLIndividual> getRenderablesForItem(OWLNamedIndividual subject,
                                                     OWLSameIndividualAxiom item,
                                                     OWLOntology ontology) {
        return Lists.newArrayList(item.getIndividuals());
    }
}
