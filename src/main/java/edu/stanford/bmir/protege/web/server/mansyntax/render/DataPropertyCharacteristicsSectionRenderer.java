package edu.stanford.bmir.protege.web.server.mansyntax.render;

import com.google.common.collect.Lists;
import org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLFunctionalDataPropertyAxiom;
import org.semanticweb.owlapi.model.OWLOntology;

import java.util.List;
import java.util.Set;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 24/02/2014
 */
public class DataPropertyCharacteristicsSectionRenderer extends AbstractOWLAxiomItemSectionRenderer<OWLDataProperty, OWLFunctionalDataPropertyAxiom, ManchesterOWLSyntax> {

    @Override
    public ManchesterOWLSyntax getSection() {
        return ManchesterOWLSyntax.CHARACTERISTICS;
    }

    @Override
    protected Set<OWLFunctionalDataPropertyAxiom> getAxiomsInOntology(OWLDataProperty subject, OWLOntology ontology) {
        return ontology.getFunctionalDataPropertyAxioms(subject);
    }

    @Override
    public List<ManchesterOWLSyntax> getRenderablesForItem(OWLDataProperty subject,
                                                           OWLFunctionalDataPropertyAxiom item, OWLOntology ontology) {
        return Lists.newArrayList(ManchesterOWLSyntax.FUNCTIONAL);
    }
}
