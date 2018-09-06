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
public class DisplayNameRenderer {

    private final DisplayNameSettingsManager displayNameSettingsManager;

    @Inject
    public DisplayNameRenderer(DisplayNameSettingsManager displayNameSettingsManager) {
        this.displayNameSettingsManager = displayNameSettingsManager;
    }

    @Nonnull
    public String getBrowserText(@Nonnull OWLPrimitiveData primitiveData) {
        return displayNameSettingsManager.getLocalDisplayNameSettings()
                                         .getPrimaryDisplayNameLanguages()
                                         .stream()
                                         .map(l -> primitiveData.getShortForms().get(l))
                                         .filter(Objects::nonNull)
                                         .findFirst()
                                         .orElse(primitiveData.getBrowserText());
    }
}
