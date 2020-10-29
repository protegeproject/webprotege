package edu.stanford.bmir.protege.web.shared.project;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-10-20
 */
public interface OntologyDocumentIdDisplayNameProvider {

    @Nonnull
    String getDisplayName(@Nonnull OntologyDocumentId documentId);
}
