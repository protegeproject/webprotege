package edu.stanford.bmir.protege.web.client.lang;

import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Map;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 29 Jul 2018
 */
public class PreferredLanguageBrowserTextRenderer {

    private final PreferredLanguageManager preferredLanguageManager;

    @Inject
    public PreferredLanguageBrowserTextRenderer(PreferredLanguageManager preferredLanguageManager) {
        this.preferredLanguageManager = preferredLanguageManager;
    }

    @Nonnull
    public String getBrowserText(@Nonnull OWLPrimitiveData primitiveData) {
        return preferredLanguageManager.getDisplayLanguage().getPrimaryLanguage()
                                       .map(primaryLang -> primitiveData.getShortForms().entrySet().stream()
                                                                        .filter(e -> e.getKey().equals(primaryLang))
                                                                        .findFirst()
                                                                        .map(Map.Entry::getValue)
                                                                        .orElse(primitiveData.getBrowserText()))
                                       .orElse(primitiveData.getBrowserText());
    }
}
