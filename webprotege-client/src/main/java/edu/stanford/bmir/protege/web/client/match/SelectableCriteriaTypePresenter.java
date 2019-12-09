package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Label;
import edu.stanford.bmir.protege.web.shared.match.criteria.Criteria;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Jun 2018
 *
 * Displays a single presenter for a criteria object at a time.  The user can choose
 * the displayed presenter via the view.
 */
public abstract class SelectableCriteriaTypePresenter<C extends Criteria> implements CriteriaPresenter<C> {

    @Nonnull
    private final SelectableCriteriaTypeView view;

    @Nonnull
    private final List<CriteriaPresenterFactory<? extends C>> presenterFactories = new ArrayList<>();

    @Nonnull
    private final Map<String, CriteriaPresenter<? extends C>> presenterMap = new HashMap<>();

    public SelectableCriteriaTypePresenter(@Nonnull SelectableCriteriaTypeView view) {
        this.view = checkNotNull(view);
    }

    @Override
    public final void start(@Nonnull AcceptsOneWidget container) {
        if (presenterFactories.isEmpty()) {
            List<CriteriaPresenterFactory<? extends C>> factories = new ArrayList<>();
            start((PresenterFactoryRegistry<C>) factories::add);
            setCriteriaPresenterFactories(factories);
        }

        view.setSelectedNameChangedHandler(this::handleSelectedNameChanged);
        container.setWidget(view);

    }

    /**
     * Start the presenter and register presenter factories
     *
     * @param factoryRegistry A registry of presenter factories.  SubClasses register specific factories.
     */
    protected abstract void start(@Nonnull PresenterFactoryRegistry<C> factoryRegistry);

    private void setCriteriaPresenterFactories(@Nonnull List<CriteriaPresenterFactory<? extends C>> presenterFactories) {
        this.presenterFactories.clear();
        stopAllPresenters();
        this.presenterMap.clear();
        this.presenterFactories.addAll(checkNotNull(presenterFactories));
        List<String> names = presenterFactories.stream()
                                               .map(CriteriaPresenterFactory::getDisplayName)
                                               .collect(Collectors.toList());
        this.view.setSelectableNames(names);
        displayFirstPresenter();
    }

    private void handleSelectedNameChanged() {
        int selIndex = view.getSelectedIndex();
        if (selIndex != -1) {
            displayPresenter(selIndex);
        }
    }

    @Override
    public void stop() {
        stopAllPresenters();
    }

    /**
     * Stops all instantiated presenters
     */
    private void stopAllPresenters() {
        this.presenterMap.values().forEach(CriteriaPresenter::stop);
    }

    /**
     * Displays the first presenter, if there is a first presenter.  If the list of possible
     * presenters is empty then the view will be blanked.
     */
    private void displayFirstPresenter() {
        if (!presenterFactories.isEmpty()) {
            view.setSelectedName(0);
            displayPresenter(0);
        }
        else {
            displayPresenter(-1);
        }
    }

    private Optional<CriteriaPresenter> displayPresenter(int index) {
        if (index != -1) {
            view.setSelectedName(index);
            CriteriaPresenterFactory<? extends C> factory = presenterFactories.get(index);
            String name = factory.getDisplayName();
            CriteriaPresenter<? extends C> presenter = presenterMap.computeIfAbsent(name, n -> factory.createPresenter());
            presenter.start(view);
            return Optional.of(presenter);
        }
        else {
            view.setWidget(new Label("Nothing to select"));
            return Optional.empty();
        }
    }

    @Override
    public Optional<? extends C> getCriteria() {
        return view.getSelectedName()
                   .map(presenterMap::get)
                   .filter(Objects::nonNull)
                   .flatMap(CriteriaPresenter::getCriteria);
    }

    @Override
    public final void setCriteria(@Nonnull C criteria) {
        CriteriaPresenterFactory<? extends C> presenterFactoryForCriteria = getPresenterFactoryForCriteria(criteria);
        int presenterIndex = -1;
        for(int i = 0; i < presenterFactories.size(); i++) {
            if(presenterFactories.get(i).getDisplayName().equals(presenterFactoryForCriteria.getDisplayName())) {
                presenterIndex = i;
                break;
            }
        }
        GWT.log("[SelectableCriteriaTypePresenter] Found presenter at " + presenterIndex);
        if(presenterIndex != -1) {
            view.setSelectedName(presenterIndex);
            displayPresenter(presenterIndex).ifPresent(presenter -> {
                presenter.setCriteria(criteria);
            });
        }
    }

    @Nonnull
    protected abstract CriteriaPresenterFactory<? extends C> getPresenterFactoryForCriteria(@Nonnull C criteria);

    public interface PresenterFactoryRegistry<C> {

        /**
         * Register a {@link CriteriaPresenterFactory} with this registry
         */
        void addPresenter(@Nonnull CriteriaPresenterFactory<? extends C> factory);
    }
}
