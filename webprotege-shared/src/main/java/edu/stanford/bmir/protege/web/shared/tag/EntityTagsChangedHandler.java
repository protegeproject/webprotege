package edu.stanford.bmir.protege.web.shared.tag;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Mar 2018
 */
public interface EntityTagsChangedHandler {

    void handleEntityTagsChanged(@Nonnull EntityTagsChangedEvent event);
}
