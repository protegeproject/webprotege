package edu.stanford.bmir.protege.web.client.entity;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-10-06
 */
public class SimilarEntitiesPresenter {

    @Nonnull
    private final SimilarEntitiesView view;

    @Inject
    public SimilarEntitiesPresenter(@Nonnull SimilarEntitiesView view) {
        this.view = checkNotNull(view);
    }
}
