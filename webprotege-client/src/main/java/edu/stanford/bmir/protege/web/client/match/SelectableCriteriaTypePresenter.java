package edu.stanford.bmir.protege.web.client.match;

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
public abstract class SelectableCriteriaTypePresenter implements CriteriaPresenter {

    @Nonnull
    private final SelectableCriteriaTypeView view;

    @Nonnull
    private final List<CriteriaPresenterFactory> presenterFactories = new ArrayList<>();

    @Nonnull
    private final Map<String, CriteriaPresenter> presenterMap = new HashMap<>();

    public SelectableCriteriaTypePresenter(@Nonnull SelectableCriteriaTypeView view) {
        this.view = checkNotNull(view);
    }

    @Override
    public final void start(@Nonnull AcceptsOneWidget container) {
        if (presenterFactories.isEmpty()) {
            List<CriteriaPresenterFactory> factories = new ArrayList<>();
            start((PresenterFactoryRegistry) factories::add);
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
    protected abstract void start(@Nonnull PresenterFactoryRegistry factoryRegistry);

    private void setCriteriaPresenterFactories(@Nonnull List<CriteriaPresenterFactory> presenterFactories) {
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

    private void displayPresenter(int index) {
        if (index != -1) {
            view.setSelectedName(index);
            CriteriaPresenterFactory factory = presenterFactories.get(index);
            String name = factory.getDisplayName();
            CriteriaPresenter presenter = presenterMap.computeIfAbsent(name, n -> factory.createPresenter());
            presenter.start(view);
        }
        else {
            view.setWidget(new Label("Nothing to select"));
        }
    }

    @Override
    public Optional<Criteria> getCriteria() {
        return view.getSelectedName()
                   .map(presenterMap::get)
                   .filter(Objects::nonNull)
                   .flatMap(CriteriaPresenter::getCriteria);
    }

    interface PresenterFactoryRegistry {

        /**
         * Register a {@link CriteriaPresenterFactory} with this registry
         */
        void addPresenter(@Nonnull CriteriaPresenterFactory factory);
    }
}
