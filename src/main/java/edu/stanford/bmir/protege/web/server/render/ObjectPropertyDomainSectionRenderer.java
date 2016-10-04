package edu.stanford.bmir.protege.web.server.render;

import com.google.common.collect.Lists;
import org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLOntology;

import java.util.List;
import java.util.Set;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 24/02/2014
 */
public class ObjectPropertyDomainSectionRenderer extends AbstractOWLAxiomItemSectionRenderer<OWLObjectProperty, OWLObjectPropertyDomainAxiom, OWLClassExpression> {

    @Override
    public ManchesterOWLSyntax getSection() {
        return ManchesterOWLSyntax.DOMAIN;
    }

    @Override
    protected Set<OWLObjectPropertyDomainAxiom> getAxiomsInOntology(OWLObjectProperty subject, OWLOntology ontology) {
        return ontology.getObjectPropertyDomainAxioms(subject);
    }

    @Override
    public List<OWLClassExpression> getRenderablesForItem(OWLObjectProperty subject,
                                                          OWLObjectPropertyDomainAxiom item,
                                                          OWLOntology ontology) {
        return Lists.newArrayList(item.getDomain());
    }

    @Override
    public String getSeparatorAfter(int renderableIndex, List<OWLClassExpression> renderables) {
        return ", ";
    }
}
