package edu.stanford.bmir.protege.web.server.mansyntax.render;

import com.google.common.collect.Lists;
import org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLSubDataPropertyOfAxiom;

import java.util.List;
import java.util.Set;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 24/02/2014
 */
public class DataPropertySubPropertyOfSectionRenderer extends AbstractOWLAxiomItemSectionRenderer<OWLDataProperty, OWLSubDataPropertyOfAxiom, OWLDataPropertyExpression> {

    @Override
    public ManchesterOWLSyntax getSection() {
        return ManchesterOWLSyntax.SUB_PROPERTY_OF;
    }

    @Override
    protected Set<OWLSubDataPropertyOfAxiom> getAxiomsInOntology(OWLDataProperty subject, OWLOntology ontology) {
        return ontology.getDataSubPropertyAxiomsForSubProperty(subject);
    }

    @Override
    public List<OWLDataPropertyExpression> getRenderablesForItem(OWLDataProperty subject,
                                                                 OWLSubDataPropertyOfAxiom item, OWLOntology ontology) {
        return Lists.newArrayList(item.getSuperProperty());
    }
}
