package edu.stanford.bmir.protege.web.server.mansyntax.render;

import com.google.common.collect.Lists;
import org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.util.ShortFormProvider;

import java.util.Comparator;
import java.util.List;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 24/02/2014
 */
public class OntologyAnnotationsSectionRenderer extends AbstractSectionRenderer<OWLOntology, OWLAnnotation, OWLObject> {

    public static final String VALUE_SEPARATOR = "<span style=\"color: darkgray;\"> : </span>";

    @Override
    public ManchesterOWLSyntax getSection() {
        return ManchesterOWLSyntax.ANNOTATIONS;
    }

    @Override
    public Formatting getSectionFormatting() {
        return Formatting.LINE_PER_ITEM;
    }

    @Override
    public List<OWLAnnotation> getItemsInOntology(OWLOntology subject,
                                                  OWLOntology ontology,
                                                  ShortFormProvider shortFormProvider,
                                                  Comparator<OWLObject> comparator) {
        return Lists.newArrayList(ontology.getAnnotations());
    }

    @Override
    public List<OWLObject> getRenderablesForItem(OWLOntology subject, OWLAnnotation item, OWLOntology ontology) {
        return Lists.newArrayList(item.getProperty(), item.getValue());
    }

    @Override
    public String getSeparatorAfter(int renderableIndex, List renderables) {
        return renderableIndex == 0 ? " " : VALUE_SEPARATOR;
    }

    @Override
    public List<OWLAnnotation> getAnnotations(OWLAnnotation item) {
        return Lists.newArrayList(item.getAnnotations());
    }
}
