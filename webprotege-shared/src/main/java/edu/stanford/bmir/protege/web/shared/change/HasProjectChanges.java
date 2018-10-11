package edu.stanford.bmir.protege.web.shared.change;

import edu.stanford.bmir.protege.web.shared.pagination.Page;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Oct 2018
 */
public interface HasProjectChanges {

    @Nonnull
    Page<ProjectChange> getProjectChanges();
}
