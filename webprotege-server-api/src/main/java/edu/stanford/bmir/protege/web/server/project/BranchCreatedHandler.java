package edu.stanford.bmir.protege.web.server.project;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-10-20
 */
public interface BranchCreatedHandler {

    void handleBranchCreated(@Nonnull BranchCreatedEvent event);
}
