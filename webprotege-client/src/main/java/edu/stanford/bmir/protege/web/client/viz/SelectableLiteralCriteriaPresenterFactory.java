package edu.stanford.bmir.protege.web.client.viz;

import edu.stanford.bmir.protege.web.client.match.CriteriaPresenter;
import edu.stanford.bmir.protege.web.client.match.CriteriaPresenterFactory;
import edu.stanford.bmir.protege.web.client.match.SelectableLiteralCriteriaPresenter;
import edu.stanford.bmir.protege.web.shared.match.criteria.LiteralCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-06-18
 */
public class SelectableLiteralCriteriaPresenterFactory implements CriteriaPresenterFactory<LiteralCriteria> {

    @Nonnull
    private final Provider<SelectableLiteralCriteriaPresenter> presenterProvider;

    @Inject
    public SelectableLiteralCriteriaPresenterFactory(@Nonnull Provider<SelectableLiteralCriteriaPresenter> presenterProvider) {
        this.presenterProvider = checkNotNull(presenterProvider);
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return "Literal that";
    }

    @Nonnull
    @Override
    public CriteriaPresenter<LiteralCriteria> createPresenter() {
        return presenterProvider.get();
    }
}
