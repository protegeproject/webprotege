package edu.stanford.bmir.protege.web.client.lang;

import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Objects;

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
        return preferredLanguageManager.getDisplayLanguage()
                                       .getPrimaryDisplayNameLanguages()
                                       .stream()
                                       .map(l -> primitiveData.getShortForms().get(l))
                                       .filter(Objects::nonNull)
                                       .findFirst()
                                       .orElse("");
    }
}
