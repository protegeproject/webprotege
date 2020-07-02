package edu.stanford.bmir.protege.web.client.form;

import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-10
 */
public interface EntityFormSubjectFactory {

    /**
     * Creates a fresh subject, if possible.
     * @return A fresh subject or an optional empty.
     */
    @Nonnull
    Optional<OWLEntity> createSubject();
}
