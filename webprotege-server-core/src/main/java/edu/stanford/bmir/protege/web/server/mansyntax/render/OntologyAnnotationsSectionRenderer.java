package edu.stanford.bmir.protege.web.server.mansyntax.render;

import com.google.common.collect.Lists;
import edu.stanford.bmir.protege.web.server.index.OntologyAnnotationsIndex;
import org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.util.ShortFormProvider;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Comparator;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 24/02/2014
 */
public class OntologyAnnotationsSectionRenderer extends AbstractSectionRenderer<OWLOntology, OWLAnnotation, OWLObject> {

    public static final String VALUE_SEPARATOR = "<span style=\"color: darkgray;\"> : </span>";

    @Nonnull
    private final OntologyAnnotationsIndex annotationsIndex;

    @Inject
    public OntologyAnnotationsSectionRenderer(@Nonnull OntologyAnnotationsIndex annotationsIndex) {
        this.annotationsIndex = checkNotNull(annotationsIndex);
    }

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
                                                  OWLOntologyID ontologyId,
                                                  ShortFormProvider shortFormProvider,
                                                  Comparator<OWLObject> comparator) {
        return annotationsIndex.getOntologyAnnotations(ontologyId).collect(toList());
    }

    @Override
    public List<OWLObject> getRenderablesForItem(OWLOntology subject, OWLAnnotation item, OWLOntologyID ontologyId) {
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
