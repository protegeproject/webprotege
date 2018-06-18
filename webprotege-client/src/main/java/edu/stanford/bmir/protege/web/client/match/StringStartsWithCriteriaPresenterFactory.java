package edu.stanford.bmir.protege.web.client.match;

import edu.stanford.bmir.protege.web.shared.match.criteria.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Jun 2018
 */
public class StringStartsWithCriteriaPresenterFactory implements CriteriaPresenterFactory<StringStartsWithCriteria> {

    @Nonnull
    private final Provider<StringStartsWithCriteriaPresenter> presenterFactory;

    @Inject
    public StringStartsWithCriteriaPresenterFactory(@Nonnull Provider<StringStartsWithCriteriaPresenter> presenterFactory) {
        this.presenterFactory = checkNotNull(presenterFactory);
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return "starts with";
    }

    @Nonnull
    @Override
    public CriteriaPresenter<StringStartsWithCriteria> createPresenter() {
        return presenterFactory.get();
    }
}
