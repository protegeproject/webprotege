package edu.stanford.bmir.protege.web.server.shortform;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.lang.ActiveLanguagesManager;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-09
 */
public class SearchableLanguagesManagerImpl implements SearchableLanguagesManager {

    @Inject
    public SearchableLanguagesManagerImpl() {
    }

    @Nonnull
    @Override
    public ImmutableList<DictionaryLanguage> getSearchableLanguages() {
        return ImmutableList.of(
                DictionaryLanguage.rdfsLabel(""),
                DictionaryLanguage.rdfsLabel("en"),
                DictionaryLanguage.skosPrefLabel("en"),
                DictionaryLanguage.localName()
        );
    }
}
