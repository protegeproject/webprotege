package edu.stanford.bmir.protege.web.server.render;

import com.google.common.collect.Lists;
import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntax;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLEquivalentDataPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLOntology;

import java.util.List;
import java.util.Set;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 24/02/2014
 */
public class DataPropertyEquivalentToSectionRenderer extends AbstractOWLAxiomItemSectionRenderer<OWLDataProperty, OWLEquivalentDataPropertiesAxiom, OWLDataPropertyExpression> {

    @Override
    public ManchesterOWLSyntax getSection() {
        return ManchesterOWLSyntax.EQUIVALENT_TO;
    }

    @Override
    protected Set<OWLEquivalentDataPropertiesAxiom> getAxiomsInOntology(OWLDataProperty subject, OWLOntology ontology) {
        return ontology.getEquivalentDataPropertiesAxioms(subject);
    }

    @Override
    public List<OWLDataPropertyExpression> getRenderablesForItem(OWLDataProperty subject,
                                                                 OWLEquivalentDataPropertiesAxiom item,
                                                                 OWLOntology ontology) {
        return Lists.newArrayList(item.getPropertiesMinus(subject));
    }
}
