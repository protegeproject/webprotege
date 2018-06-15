package edu.stanford.bmir.protege.web.client.match;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.match.criteria.Criteria;

import javax.annotation.Nonnull;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Jun 2018
 *
 * Presents a list of criteria, delegating to a list of {@link CriteriaPresenter}s
 * to do this
 */
public abstract class CriteriaListPresenter<C extends Criteria> implements CriteriaPresenter<C> {

    @Nonnull
    private final CriteriaListView view;

    @Nonnull
    private final Provider<CriteriaListCriteriaViewContainer> viewContainerProvider;

    @Nonnull
    private final CriteriaPresenterFactory<C> presenterFactory;

    @Nonnull
    private final List<CriteriaPresenter<? extends C>> criteriaPresenters = new ArrayList<>();

    public CriteriaListPresenter(@Nonnull CriteriaListView view,
                                 @Nonnull Provider<CriteriaListCriteriaViewContainer> viewContainerProvider,
                                 @Nonnull CriteriaPresenterFactory<C> presenterFactory) {
        this.view = checkNotNull(view);
        this.viewContainerProvider = checkNotNull(viewContainerProvider);
        this.presenterFactory = checkNotNull(presenterFactory);
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        if (criteriaPresenters.isEmpty()) {
            addCriteriaPresenter();
        }
        view.setAddCriteriaHandler(this::handleAddCriteria);
    }

    private void handleAddCriteria() {
        addCriteriaPresenter();
    }

    private void addCriteriaPresenter() {
        CriteriaPresenter<? extends C> presenter = presenterFactory.createPresenter();
        CriteriaListCriteriaViewContainer viewContainer = viewContainerProvider.get();
        presenter.start(viewContainer);
        view.addCriteriaView(viewContainer);
        criteriaPresenters.add(presenter);
        if (criteriaPresenters.size() > 1) {
            viewContainer.setRemoveButtonVisible(true);
            viewContainer.setRemoveHandler(() -> removeCriteria(presenter));
        }
    }

    private void removeCriteria(@Nonnull CriteriaPresenter<? extends C> presenter) {
        int presenterIndex = criteriaPresenters.indexOf(presenter);
        criteriaPresenters.remove(presenterIndex);
        presenter.stop();
        view.removeCriteriaView(presenterIndex);
    }

    @Override
    public void stop() {
        criteriaPresenters.forEach(CriteriaPresenter::stop);
    }

    @Override
    public Optional<? extends C> getCriteria() {
        ImmutableList.Builder<C> builder = ImmutableList.builder();
        for(CriteriaPresenter<? extends C> presenter : criteriaPresenters) {
            presenter.getCriteria().ifPresent(builder::add);
        }
        ImmutableList<? extends C> criteria = builder.build();
        if (criteria.isEmpty()) {
            return Optional.empty();
        }
        else {
            C compositeCriteria = createCriteria(criteria);
            return Optional.of(compositeCriteria);
        }
    }

    protected abstract C createCriteria(@Nonnull ImmutableList<? extends C> criteria);
}
