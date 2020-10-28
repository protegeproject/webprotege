package edu.stanford.bmir.protege.web.server.project;

import edu.stanford.bmir.protege.web.server.index.Index;
import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-15
 */
public interface DefaultOntologyIdManager extends Index {

    @Nonnull
    OntologyDocumentId getDefaultOntologyDocumentId();
}
