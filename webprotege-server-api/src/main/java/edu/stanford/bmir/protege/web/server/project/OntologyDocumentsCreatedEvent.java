package edu.stanford.bmir.protege.web.server.project;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-10-20
 */
@AutoValue
public abstract class OntologyDocumentsCreatedEvent {

    @Nonnull
    public abstract ImmutableSet<OntologyDocumentId> getOntologyDocumentIds();
}
