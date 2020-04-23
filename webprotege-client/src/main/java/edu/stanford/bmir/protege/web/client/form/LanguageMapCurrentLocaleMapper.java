package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.i18n.client.LocaleInfo;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-23
 */
public class LanguageMapCurrentLocaleMapper {

    @Inject
    public LanguageMapCurrentLocaleMapper() {
    }

    @Nonnull
    public String getValueForCurrentLocale(LanguageMap languageMap) {
        LocaleInfo localeInfo = LocaleInfo.getCurrentLocale();
        String langTag = localeInfo.getLocaleName();
        return languageMap.get(langTag);
    }
}
