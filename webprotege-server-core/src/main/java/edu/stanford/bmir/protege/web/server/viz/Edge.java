package edu.stanford.bmir.protege.web.server.viz;

import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Oct 2018
 */
public interface Edge {

    @Nonnull
    OWLEntityData getHead();

    OWLEntityData getTail();

    String getRelationshipDescriptor();

    @Nonnull
    String getLabel();

    boolean isIsA();
}
