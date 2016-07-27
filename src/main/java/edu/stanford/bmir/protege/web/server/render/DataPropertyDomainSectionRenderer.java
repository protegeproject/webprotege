package edu.stanford.bmir.protege.web.server.render;

import com.google.common.collect.Lists;
import org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLOntology;

import java.util.List;
import java.util.Set;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 24/02/2014
 */
public class DataPropertyDomainSectionRenderer extends AbstractOWLAxiomItemSectionRenderer<OWLDataProperty, OWLDataPropertyDomainAxiom, OWLClassExpression> {

    @Override
    public ManchesterOWLSyntax getSection() {
        return ManchesterOWLSyntax.DOMAIN;
    }

    @Override
    protected Set<OWLDataPropertyDomainAxiom> getAxiomsInOntology(OWLDataProperty subject, OWLOntology ontology) {
        return ontology.getDataPropertyDomainAxioms(subject);
    }

    @Override
    public List<OWLClassExpression> getRenderablesForItem(OWLDataProperty subject,
                                                          OWLDataPropertyDomainAxiom item,
                                                          OWLOntology ontology) {
        return Lists.newArrayList(item.getDomain());
    }
}
