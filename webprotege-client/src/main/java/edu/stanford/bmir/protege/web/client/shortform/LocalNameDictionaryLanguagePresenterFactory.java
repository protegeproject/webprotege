package edu.stanford.bmir.protege.web.client.shortform;

import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.shortform.LocalNameDictionaryLanguage;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-10
 */
public class LocalNameDictionaryLanguagePresenterFactory implements DictionaryLanguagePresenterFactory {

    @Nonnull
    private final Provider<LocalNameDictionaryLanguagePresenter> presenterProvider;

    @Inject
    public LocalNameDictionaryLanguagePresenterFactory(@Nonnull Provider<LocalNameDictionaryLanguagePresenter> presenterProvider) {
        this.presenterProvider = checkNotNull(presenterProvider);
    }

    @Nonnull
    @Override
    public String getName() {
        return "Local name";
    }

    @Override
    public boolean isFactoryFor(@Nonnull DictionaryLanguage dictionaryLanguage) {
        return dictionaryLanguage instanceof LocalNameDictionaryLanguage;
    }

    @Nonnull
    @Override
    public DictionaryLanguagePresenter createPresenter() {
        return presenterProvider.get();
    }
}
