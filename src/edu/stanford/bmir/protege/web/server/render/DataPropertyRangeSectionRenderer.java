package edu.stanford.bmir.protege.web.server.render;

import com.google.common.collect.Lists;
import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntax;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLOntology;

import java.util.List;
import java.util.Set;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 24/02/2014
 */
public class DataPropertyRangeSectionRenderer extends AbstractOWLAxiomItemSectionRenderer<OWLDataProperty, OWLDataPropertyRangeAxiom, OWLDataRange> {

    @Override
    public ManchesterOWLSyntax getSection() {
        return ManchesterOWLSyntax.RANGE;
    }

    @Override
    protected Set<OWLDataPropertyRangeAxiom> getAxiomsInOntology(OWLDataProperty subject, OWLOntology ontology) {
        return ontology.getDataPropertyRangeAxioms(subject);
    }

    @Override
    public List<OWLDataRange> getRenderablesForItem(OWLDataProperty subject,
                                                    OWLDataPropertyRangeAxiom item,
                                                    OWLOntology ontology) {
        return Lists.newArrayList(item.getRange());
    }
}
