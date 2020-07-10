package edu.stanford.bmir.protege.web.server.shortform;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-09
 */
public interface SearchableLanguagesManager {

    @Nonnull
    ImmutableList<DictionaryLanguage> getSearchableLanguages();
}
