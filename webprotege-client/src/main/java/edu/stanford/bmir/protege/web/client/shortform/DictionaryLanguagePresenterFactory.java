package edu.stanford.bmir.protege.web.client.shortform;

import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-10
 */
public interface DictionaryLanguagePresenterFactory {

    @Nonnull
    String getName();

    boolean isFactoryFor(@Nonnull DictionaryLanguage dictionaryLanguage);

    @Nonnull
    DictionaryLanguagePresenter createPresenter();
}
