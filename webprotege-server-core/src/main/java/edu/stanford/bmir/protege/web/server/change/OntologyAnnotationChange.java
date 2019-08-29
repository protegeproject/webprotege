package edu.stanford.bmir.protege.web.server.change;

import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-26
 */
public interface OntologyAnnotationChange extends OntologyChange {

    @Nonnull
    OWLAnnotation getAnnotation();

    @Nonnull
    @Override
    default Set<OWLEntity> getSignature() {
        return getAnnotation().getSignature();
    }

    @Nonnull
    @Override
    default OWLAnnotation getAnnotationOrThrow() {
        return getAnnotation();
    }
}
