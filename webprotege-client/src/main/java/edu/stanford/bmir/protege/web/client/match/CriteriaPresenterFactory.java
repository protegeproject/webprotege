package edu.stanford.bmir.protege.web.client.match;

import edu.stanford.bmir.protege.web.shared.match.criteria.Criteria;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Jun 2018
 */
public interface CriteriaPresenterFactory<C extends Criteria> {

    @Nonnull
    String getDisplayName();

    @Nonnull
    CriteriaPresenter<? extends C> createPresenter();
}
