package edu.stanford.bmir.protege.web.client.shortform;

import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.shortform.OboIdDictionaryLanguage;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-10
 */
public class OboIdIDictionaryLanguagePresenterFactory implements DictionaryLanguagePresenterFactory {

    @Nonnull
    private final Provider<OboIdDictionaryLanguagePresenter> presenterProvider;

    @Inject
    public OboIdIDictionaryLanguagePresenterFactory(@Nonnull Provider<OboIdDictionaryLanguagePresenter> presenterProvider) {
        this.presenterProvider = checkNotNull(presenterProvider);
    }

    @Nonnull
    @Override
    public String getName() {
        return "OBO Id";
    }

    @Override
    public boolean isFactoryFor(@Nonnull DictionaryLanguage dictionaryLanguage) {
        return dictionaryLanguage instanceof OboIdDictionaryLanguage;
    }

    @Nonnull
    @Override
    public DictionaryLanguagePresenter createPresenter() {
        return presenterProvider.get();
    }
}
