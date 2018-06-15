package edu.stanford.bmir.protege.web.client.match;

import edu.stanford.bmir.protege.web.shared.match.criteria.IriCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Jun 2018
 */
public class IriHasAnnotationsCriteriaPresenterFactory implements CriteriaPresenterFactory<IriCriteria> {

    @Nonnull
    private final Provider<IriHasAnnotationsCriteriaPresenter> presenterProvider;

    @Inject
    public IriHasAnnotationsCriteriaPresenterFactory(@Nonnull Provider<IriHasAnnotationsCriteriaPresenter> presenterProvider) {
        this.presenterProvider = checkNotNull(presenterProvider);
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return "has value with annotation";
    }

    @Nonnull
    @Override
    public CriteriaPresenter<IriCriteria> createPresenter() {
        return presenterProvider.get();
    }
}
