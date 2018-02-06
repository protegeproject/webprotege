package edu.stanford.bmir.protege.web.server.mansyntax.render;

import com.google.common.collect.Lists;
import org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax;
import org.semanticweb.owlapi.model.OWLDisjointObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;

import java.util.List;
import java.util.Set;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 24/02/2014
 */
public class ObjectPropertyDisjointWithSectionRenderer extends AbstractOWLAxiomItemSectionRenderer<OWLObjectProperty, OWLDisjointObjectPropertiesAxiom, OWLObjectPropertyExpression> {

    @Override
    public ManchesterOWLSyntax getSection() {
        return ManchesterOWLSyntax.DISJOINT_WITH;
    }

    @Override
    protected Set<OWLDisjointObjectPropertiesAxiom> getAxiomsInOntology(OWLObjectProperty subject,
                                                                        OWLOntology ontology) {
        return ontology.getDisjointObjectPropertiesAxioms(subject);
    }

    @Override
    public List<OWLObjectPropertyExpression> getRenderablesForItem(OWLObjectProperty subject,
                                                                   OWLDisjointObjectPropertiesAxiom item,
                                                                   OWLOntology ontology) {
        return Lists.newArrayList(item.getPropertiesMinus(subject));
    }

    @Override
    public String getSeparatorAfter(int renderableIndex, List<OWLObjectPropertyExpression> renderables) {
        return ", ";
    }
}
