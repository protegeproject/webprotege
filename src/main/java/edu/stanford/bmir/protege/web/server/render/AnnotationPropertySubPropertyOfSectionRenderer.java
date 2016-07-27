package edu.stanford.bmir.protege.web.server.render;

import com.google.common.collect.Lists;
import org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax;
import org.semanticweb.owlapi.model.*;

import java.util.List;
import java.util.Set;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 24/02/2014
 */
public class AnnotationPropertySubPropertyOfSectionRenderer extends AbstractOWLAxiomItemSectionRenderer<OWLAnnotationProperty, OWLSubAnnotationPropertyOfAxiom, OWLAnnotationProperty> {

    @Override
    protected Set<OWLSubAnnotationPropertyOfAxiom> getAxiomsInOntology(OWLAnnotationProperty subject,
                                                                       OWLOntology ontology) {
        return ontology.getSubAnnotationPropertyOfAxioms(subject);
    }

    @Override
    public ManchesterOWLSyntax getSection() {
        return ManchesterOWLSyntax.SUB_PROPERTY_OF;
    }

    @Override
    public List<OWLAnnotationProperty> getRenderablesForItem(OWLAnnotationProperty subject,
                                                             OWLSubAnnotationPropertyOfAxiom item,
                                                             OWLOntology ontology) {
        return Lists.newArrayList(item.getSuperProperty());
    }
}
