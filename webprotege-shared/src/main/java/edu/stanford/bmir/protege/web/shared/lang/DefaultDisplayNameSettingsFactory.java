package edu.stanford.bmir.protege.web.shared.lang;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguageData;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Sep 2018
 */
public class DefaultDisplayNameSettingsFactory {

    @Inject
    public DefaultDisplayNameSettingsFactory() {
    }

    @Nonnull
    public DisplayNameSettings getDefaultDisplayNameSettings(@Nonnull String langTag) {
        ImmutableList<DictionaryLanguage> primaryLanguages;
        if (langTag.isEmpty()) {
            primaryLanguages = ImmutableList.of(DictionaryLanguage.rdfsLabel(langTag),
                                                DictionaryLanguage.prefixedName(),
                                                DictionaryLanguage.localName());
        }
        else {
            primaryLanguages = ImmutableList.of(DictionaryLanguage.rdfsLabel(langTag),
                                                DictionaryLanguage.rdfsLabel(""),
                                                DictionaryLanguage.prefixedName(),
                                                DictionaryLanguage.localName());
        }
        return DisplayNameSettings.get(
                primaryLanguages,
                ImmutableList.of()
        );
    }
}
