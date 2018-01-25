package edu.stanford.bmir.protege.web.server.mansyntax.render;

import com.google.common.collect.Lists;
import org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLOntology;

import java.util.List;
import java.util.Set;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 24/02/2014
 */
public class ObjectPropertyRangeSectionRenderer extends AbstractOWLAxiomItemSectionRenderer<OWLObjectProperty, OWLObjectPropertyRangeAxiom, OWLClassExpression> {

    @Override
    public ManchesterOWLSyntax getSection() {
        return ManchesterOWLSyntax.RANGE;
    }

    @Override
    protected Set<OWLObjectPropertyRangeAxiom> getAxiomsInOntology(OWLObjectProperty subject, OWLOntology ontology) {
        return ontology.getObjectPropertyRangeAxioms(subject);
    }

    @Override
    public List<OWLClassExpression> getRenderablesForItem(OWLObjectProperty subject,
                                                          OWLObjectPropertyRangeAxiom item,
                                                          OWLOntology ontology) {
        return Lists.newArrayList(item.getRange());
    }
}
