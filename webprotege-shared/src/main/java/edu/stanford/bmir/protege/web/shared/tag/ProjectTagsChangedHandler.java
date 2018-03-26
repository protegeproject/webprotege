package edu.stanford.bmir.protege.web.shared.tag;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26 Mar 2018
 */
public interface ProjectTagsChangedHandler {

    void handleProjectTagsChanged(@Nonnull ProjectTagsChangedEvent event);
}
