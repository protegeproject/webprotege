package edu.stanford.bmir.protege.web.server.project;

import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;
import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentIdDisplayNameProvider;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-10-26
 */
public class OntologyDocumentIdDisplayNameProviderImpl implements OntologyDocumentIdDisplayNameProvider {

    @Inject
    public OntologyDocumentIdDisplayNameProviderImpl() {
    }

    @Nonnull
    @Override
    public String getDisplayName(@Nonnull OntologyDocumentId documentId) {
        return documentId.getId();
    }
}
