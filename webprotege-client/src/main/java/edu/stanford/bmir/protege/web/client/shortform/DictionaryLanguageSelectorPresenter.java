package edu.stanford.bmir.protege.web.client.shortform;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-10
 */
public class DictionaryLanguageSelectorPresenter implements HasRequestFocus {

    @Nonnull
    private final DictionaryLanguageSelectorView view;

    private final ImmutableSet<DictionaryLanguagePresenterFactory> factories;

    private final Map<DictionaryLanguagePresenterFactory, DictionaryLanguagePresenter> factoryPresenterMap = new HashMap<>();

    private Optional<DictionaryLanguagePresenter> currentPresenter = Optional.empty();

    @Inject
    public DictionaryLanguageSelectorPresenter(@Nonnull DictionaryLanguageSelectorView view,
                                               @Nonnull Set<DictionaryLanguagePresenterFactory> factories) {
        this.view = checkNotNull(view);
        this.factories = ImmutableSet.copyOf(checkNotNull(factories));
        ImmutableList<String> factoryNames = factories.stream()
                 .map(DictionaryLanguagePresenterFactory::getName)
                 .collect(toImmutableList());
        this.view.setTypeNames(factoryNames);
        this.view.setTypeNameChangedHandler(this::handleTypeNameChanged);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
    }

    private void handleTypeNameChanged() {
        Optional<String> typeName = view.getSelectedTypeName();
        typeName.ifPresent(s -> factories.stream().filter(f -> f.getName().equals(s)).findFirst().ifPresent(f -> {
            DictionaryLanguagePresenter presenter = getPresenterForFactory(f);
            presenter.start(view.getTypeDetailsContainer());
            currentPresenter = Optional.of(presenter);
        }));
    }

    @Nonnull
    public IsWidget getView() {
        return view;
    }

    public void clear() {
        view.clear();
        currentPresenter = Optional.empty();
    }

    public void setDictionaryLanguage(@Nonnull DictionaryLanguage dictionaryLanguage) {
        view.clear();
        displayPresenterFor(dictionaryLanguage);
    }

    private void displayPresenterFor(@Nonnull DictionaryLanguage language) {
        DictionaryLanguagePresenter presenter = findPresenter(language);
        presenter.setDictionaryLanguage(language);
        presenter.start(view.getTypeDetailsContainer());
        currentPresenter = Optional.of(presenter);
        factories.stream()
                 .filter(f -> f.isFactoryFor(language))
                 .findFirst()
                 .ifPresent(f -> view.setSelectedTypeName(f.getName()));
    }

    private DictionaryLanguagePresenter findPresenter(@Nonnull DictionaryLanguage language) {
        for(DictionaryLanguagePresenterFactory factory : factories) {
            if(factory.isFactoryFor(language)) {
                return getPresenterForFactory(factory);
            }
        }
        throw new RuntimeException("No suitable factory found for " + language);
    }

    private DictionaryLanguagePresenter getPresenterForFactory(DictionaryLanguagePresenterFactory factory) {
        DictionaryLanguagePresenter presenter = factoryPresenterMap.get(factory);
        if(presenter == null) {
            presenter = factory.createPresenter();
            factoryPresenterMap.put(factory, presenter);
        }
        return presenter;
    }

    @Nonnull
    public Optional<DictionaryLanguage> getDictionaryLanguage() {
        return currentPresenter.flatMap(DictionaryLanguagePresenter::getDictionaryLanguage);
    }

    @Override
    public void requestFocus() {
        view.requestFocus();
    }
}
