package edu.stanford.bmir.protege.web.server.shortform;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;

import javax.inject.Inject;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 6 Apr 2018
 */
@ProjectSingleton
public class LanguageManager {

    private static final String ANY_LANG = "*";

    private final ImmutableList<DictionaryLanguage> languages = ImmutableList.of(
            DictionaryLanguage.rdfsLabel("en"),
            DictionaryLanguage.skosPrefLabel("en"),
            DictionaryLanguage.rdfsLabel(""),
            DictionaryLanguage.skosPrefLabel(""),
            DictionaryLanguage.localName()
    );

    @Inject
    public LanguageManager() {
    }

    public ImmutableList<DictionaryLanguage> getLanguages() {
        return languages;
    }
}
