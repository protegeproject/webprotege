package edu.stanford.bmir.protege.web.server.obo;

import edu.stanford.bmir.protege.web.server.index.ProjectAnnotationAssertionAxiomsBySubjectIndex;
import edu.stanford.bmir.protege.web.shared.obo.OBOXRef;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
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
    private ProjectAnnotationAssertionAxiomsBySubjectIndex annotationAssertionsIndex;

    @Inject
    public XRefExtractor(@Nonnull AnnotationToXRefConverter converter,
                         @Nonnull ProjectAnnotationAssertionAxiomsBySubjectIndex annotationAssertionsIndex) {
        this.converter = checkNotNull(converter);
        this.annotationAssertionsIndex = checkNotNull(annotationAssertionsIndex);
    }

    @Nonnull
    public List<OBOXRef> getXRefs(@Nonnull OWLAnnotationAssertionAxiom annotationAssertion) {
        return annotationAssertion.getAnnotations().stream()
                                  .filter(anno -> isXRefProperty(anno.getProperty()))
                                  .map(anno -> converter.toXRef(anno, anno.getAnnotations()))
                                  .collect(toList());
    }

    @Nonnull
    public List<OBOXRef> getXRefs(@Nonnull OWLEntity term) {
        return annotationAssertionsIndex.getAnnotationAssertionAxioms(term.getIRI())
                .filter(ax -> isXRefProperty(ax.getProperty()))
                .map(ax -> converter.toXRef(ax.getAnnotation(), ax.getAnnotations()))
                .collect(toList());
    }
}
