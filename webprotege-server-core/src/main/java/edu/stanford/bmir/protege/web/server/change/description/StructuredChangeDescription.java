package edu.stanford.bmir.protege.web.server.change.description;

import edu.stanford.bmir.protege.web.server.owlapi.OWLObjectStringFormatter;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2018-12-10
 */
public interface StructuredChangeDescription {

    @Nonnull
    String getTypeName();

    @Nonnull
    String formatDescription(@Nonnull OWLObjectStringFormatter formatter);
}
