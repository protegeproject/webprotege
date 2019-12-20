package edu.stanford.bmir.protege.web.client.viz;

import edu.stanford.bmir.protege.web.client.match.CriteriaPresenter;
import edu.stanford.bmir.protege.web.client.match.CriteriaPresenterFactory;
import edu.stanford.bmir.protege.web.shared.viz.TailNodeMatchesCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-08
 */
public class TailNodeMatchesCriteriaPresenterFactory implements CriteriaPresenterFactory<TailNodeMatchesCriteria> {

    @Nonnull
    private final Provider<TailNodeMatchesCriteriaPresenter> presenterProvider;

    @Inject
    public TailNodeMatchesCriteriaPresenterFactory(@Nonnull Provider<TailNodeMatchesCriteriaPresenter> presenterProvider) {
        this.presenterProvider = checkNotNull(presenterProvider);
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return "Tail node entity";
    }

    @Nonnull
    @Override
    public CriteriaPresenter<? extends TailNodeMatchesCriteria> createPresenter() {
        return presenterProvider.get();
    }
}
