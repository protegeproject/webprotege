package edu.stanford.bmir.protege.web.client.match;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Jun 2018
 */
public interface CriteriaPresenterFactory {

    @Nonnull
    String getDisplayName();

    @Nonnull
    CriteriaPresenter createPresenter();
}
