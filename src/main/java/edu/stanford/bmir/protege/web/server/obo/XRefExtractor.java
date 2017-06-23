package edu.stanford.bmir.protege.web.server.obo;

import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
import edu.stanford.bmir.protege.web.shared.obo.OBOXRef;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;

import static edu.stanford.bmir.protege.web.server.obo.OboUtil.isXRefProperty;
import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Jun 2017
 */
public class XRefExtractor {

    @Nonnull
    private final AnnotationToXRefConverter converter;

    @Nonnull
    private final OWLOntology rootOntology;

    @Inject
    public XRefExtractor(@Nonnull AnnotationToXRefConverter converter,
                         @Nonnull @RootOntology OWLOntology rootOntology) {
        this.converter = converter;
        this.rootOntology = rootOntology;
    }

    @Nonnull
    public List<OBOXRef> getXRefs(@Nonnull OWLAnnotationAssertionAxiom annotationAssertion) {
        return annotationAssertion.getAnnotations().stream()
                                  .filter(anno -> isXRefProperty(anno.getProperty()))
                                  .map(anno -> converter.toXRef(anno, anno.getAnnotations()))
                                  .filter(xref -> xref != null)
                                  .collect(toList());
    }

    @Nonnull
    public List<OBOXRef> getXRefs(@Nonnull OWLEntity term) {
        return rootOntology.getAnnotationAssertionAxioms(term.getIRI()).stream()
                .filter(ax -> isXRefProperty(ax.getProperty()))
                .map(ax -> converter.toXRef(ax.getAnnotation(), ax.getAnnotations()))
                .collect(toList());
    }
}
