package edu.stanford.bmir.protege.web.shared.lang;

import com.google.common.collect.ImmutableList;
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
        ImmutableList<DictionaryLanguageData> primaryLanguages;
        if (langTag.isEmpty()) {
            primaryLanguages = ImmutableList.of(DictionaryLanguageData.rdfsLabel(langTag),
                                                DictionaryLanguageData.localName());
        }
        else {
            primaryLanguages = ImmutableList.of(DictionaryLanguageData.rdfsLabel(langTag),
                                                DictionaryLanguageData.rdfsLabel(""),
                                                DictionaryLanguageData.localName());
        }
        return DisplayNameSettings.get(
                primaryLanguages,
                ImmutableList.of()
        );
    }
}
