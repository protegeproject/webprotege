package edu.stanford.bmir.protege.web.server.render;

import com.google.common.collect.Sets;
import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntax;
import org.semanticweb.owlapi.model.*;

import java.util.List;
import java.util.Set;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 24/02/2014
 */
public class ObjectPropertySubPropertyChainSectionRenderer extends AbstractOWLAxiomItemSectionRenderer<OWLObjectProperty, OWLSubPropertyChainOfAxiom, OWLObjectPropertyExpression> {

    @Override
    public ManchesterOWLSyntax getSection() {
        return ManchesterOWLSyntax.SUB_PROPERTY_CHAIN;
    }

    @Override
    public Set<OWLSubPropertyChainOfAxiom> getAxiomsInOntology(OWLObjectProperty subject,
                                                               OWLOntology ontology) {
        Set<OWLSubPropertyChainOfAxiom> result = Sets.newHashSet();
        for(OWLSubPropertyChainOfAxiom ax : ontology.getAxioms(AxiomType.SUB_PROPERTY_CHAIN_OF)) {
            if(ax.getSuperProperty().equals(subject)) {
                result.add(ax);
            }
        }
        return result;
    }

    @Override
    public List<OWLObjectPropertyExpression> getRenderablesForItem(OWLObjectProperty subject,
                                                                   OWLSubPropertyChainOfAxiom item,
                                                                   OWLOntology ontology) {
        return item.getPropertyChain();
    }

    @Override
    public String getSeparatorAfter(int renderableIndex, List<OWLObjectPropertyExpression> renderables) {
        return "<span class=\"ms-connective-kw\"> o </span>";
    }
}
