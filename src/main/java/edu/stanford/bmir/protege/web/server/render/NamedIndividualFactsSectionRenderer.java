package edu.stanford.bmir.protege.web.server.render;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLPropertyAssertionAxiom;

import java.util.List;
import java.util.Set;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 24/02/2014
 */
public class NamedIndividualFactsSectionRenderer extends AbstractOWLAxiomItemSectionRenderer<OWLNamedIndividual, OWLPropertyAssertionAxiom<?, ?>, OWLObject> {

    @Override
    public ManchesterOWLSyntax getSection() {
        return ManchesterOWLSyntax.FACTS;
    }

    @Override
    public Set<OWLPropertyAssertionAxiom<?, ?>> getAxiomsInOntology(OWLNamedIndividual subject,
                                                                    OWLOntology ontology) {
        return Sets.<OWLPropertyAssertionAxiom<?, ?>>union(
                ontology.getObjectPropertyAssertionAxioms(subject),
                ontology.getDataPropertyAssertionAxioms(subject)
        );
    }

    @Override
    public List<OWLObject> getRenderablesForItem(OWLNamedIndividual subject,
                                                 OWLPropertyAssertionAxiom<?, ?> item,
                                                 OWLOntology ontology) {
        return Lists.newArrayList(item.getProperty(), item.getObject());
    }

    @Override
    public String getSeparatorAfter(int renderableIndex, List<OWLObject> renderables) {
        return renderableIndex == 0 ? "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" : super.getSeparatorAfter(renderableIndex, renderables);
    }
}
