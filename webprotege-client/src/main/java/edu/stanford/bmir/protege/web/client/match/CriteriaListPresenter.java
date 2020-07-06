package edu.stanford.bmir.protege.web.client.match;

import com.google.common.collect.ImmutableList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.match.criteria.Criteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.MultiMatchType;

import javax.annotation.Nonnull;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Jun 2018
 *
 * Presents a list of criteria, delegating to a list of {@link CriteriaPresenter}s
 * to do this
 */
public abstract class CriteriaListPresenter<C extends Criteria, F extends C> implements CriteriaPresenter<F> {

    @Nonnull
    private MultiMatchType defaultMatchType = MultiMatchType.ALL;

    @Nonnull
    private final CriteriaListView view;

    @Nonnull
    private final Provider<CriteriaListCriteriaViewContainer> viewContainerProvider;

    @Nonnull
    private final CriteriaPresenterFactory<C> presenterFactory;

    @Nonnull
    private final List<CriteriaPresenter<? extends C>> criteriaPresenters = new ArrayList<>();

    @Nonnull
    private final List<CriteriaListCriteriaViewContainer> viewContainers = new ArrayList<>();

    private boolean displayAtLeastOneCriteria = true;

    public CriteriaListPresenter(@Nonnull CriteriaListView view,
                                 @Nonnull Provider<CriteriaListCriteriaViewContainer> viewContainerProvider,
                                 @Nonnull CriteriaPresenterFactory<C> presenterFactory) {
        this.view = checkNotNull(view);
        this.viewContainerProvider = checkNotNull(viewContainerProvider);
        this.presenterFactory = checkNotNull(presenterFactory);
    }

    public void setDisplayAtLeastOneCriteria(boolean displayAtLeastOneCriteria) {
        this.displayAtLeastOneCriteria = displayAtLeastOneCriteria;
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        if (displayAtLeastOneCriteria && criteriaPresenters.isEmpty()) {
            addCriteriaPresenter();
        }
        view.setMultiMatchType(defaultMatchType);
        view.setAddCriteriaHandler(this::handleAddCriteria);
    }

    public void setMatchTextPrefix(@Nonnull String prefix) {
        view.setMatchTextPrefix(checkNotNull(prefix));
    }

    public void setDefaultMatchType(@Nonnull MultiMatchType defaultMatchType) {
        this.defaultMatchType = checkNotNull(defaultMatchType);
    }

    private void handleAddCriteria() {
        addCriteriaPresenter();
    }

    private void addCriteriaPresenter() {
        CriteriaPresenter<? extends C> presenter = presenterFactory.createPresenter();
        addCriteriaPresenter(presenter);
    }

    private void addCriteriaPresenter(CriteriaPresenter<? extends C> presenter) {
        CriteriaListCriteriaViewContainer viewContainer = viewContainerProvider.get();
        presenter.start(viewContainer);
        view.addCriteriaView(viewContainer);
        criteriaPresenters.add(presenter);
        viewContainers.add(viewContainer);
        viewContainer.setRemoveHandler(() -> removeCriteria(presenter));
        updateRemoveButtonVisibility();
    }

    private void removeCriteria(@Nonnull CriteriaPresenter<? extends C> presenter) {
        int presenterIndex = criteriaPresenters.indexOf(presenter);
        criteriaPresenters.remove(presenterIndex);
        viewContainers.remove(presenterIndex);
        presenter.stop();
        view.removeCriteriaView(presenterIndex);
        updateRemoveButtonVisibility();
    }

    public void clear() {
        criteriaPresenters.forEach(CriteriaPresenter::stop);
        criteriaPresenters.clear();
        viewContainers.clear();
        view.removeAllCriteriaViews();
        updateRemoveButtonVisibility();
    }

    private void updateRemoveButtonVisibility() {
        boolean removeVisible = !displayAtLeastOneCriteria || viewContainers.size() > 1;
        viewContainers.forEach(c -> c.setRemoveButtonVisible(removeVisible));
    }

    @Nonnull
    public MultiMatchType getMultiMatchType() {
        return view.getMultiMatchType();
    }

    @Override
    public void stop() {
        criteriaPresenters.forEach(CriteriaPresenter::stop);
    }

    @Override
    public Optional<? extends F> getCriteria() {
        ImmutableList.Builder<C> builder = ImmutableList.builder();
        for(CriteriaPresenter<? extends C> presenter : criteriaPresenters) {
            presenter.getCriteria().ifPresent(builder::add);
        }
        ImmutableList<? extends C> criteria = builder.build();
        if (criteria.isEmpty()) {
            return Optional.empty();
        }
        else {
            F compositeCriteria = createCompositeCriteria(criteria);
            return Optional.of(compositeCriteria);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setCriteria(@Nonnull F criteria) {
        clear();
        ImmutableList<? extends C> decomposedCriteria = decomposeCompositeCriteria(criteria);
        view.setMultiMatchType(getMultiMatchType(criteria));
        decomposedCriteria.forEach(c -> {
            CriteriaPresenter presenter = presenterFactory.createPresenter();
            addCriteriaPresenter(presenter);
            presenter.setCriteria(c);
        });
    }

    protected abstract F createCompositeCriteria(@Nonnull ImmutableList<? extends C> criteriaList);

    protected abstract ImmutableList<? extends C> decomposeCompositeCriteria(F compositeCriteria);

    protected abstract MultiMatchType getMultiMatchType(F compositeCriteria);
}
