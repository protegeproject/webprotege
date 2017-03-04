package edu.stanford.bmir.protege.web.server.mansyntax.render;

import com.google.common.collect.Lists;
import org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax;
import org.semanticweb.owlapi.model.OWLInverseObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;

import java.util.List;
import java.util.Set;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 24/02/2014
 */
public class ObjectPropertyInverseOfSectionRenderer extends AbstractOWLAxiomItemSectionRenderer<OWLObjectProperty, OWLInverseObjectPropertiesAxiom, OWLObjectPropertyExpression> {

    @Override
    public ManchesterOWLSyntax getSection() {
        return ManchesterOWLSyntax.INVERSE_OF;
    }

    @Override
    protected Set<OWLInverseObjectPropertiesAxiom> getAxiomsInOntology(OWLObjectProperty subject,
                                                                       OWLOntology ontology) {
        return ontology.getInverseObjectPropertyAxioms(subject);
    }

    @Override
    public List<OWLObjectPropertyExpression> getRenderablesForItem(OWLObjectProperty subject,
                                                                   OWLInverseObjectPropertiesAxiom item,
                                                                   OWLOntology ontology) {
        return Lists.newArrayList(item.getPropertiesMinus(subject));
    }
}
