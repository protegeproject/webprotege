package edu.stanford.bmir.protege.web.client.lang;

import com.google.gwt.core.client.GWT;
import com.google.gwt.storage.client.Storage;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.lang.DisplayDictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.lang.DisplayLanguageChangedEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 29 Jul 2018
 */
@ProjectSingleton
public class PreferredLanguageManager {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final EventBus eventBus;

    @Nonnull
    private final DisplayLanguageStorage displayLanguageStorage;

    @Nonnull
    private DisplayDictionaryLanguage displayLanguage = DisplayDictionaryLanguage.empty();

    private boolean loaded = false;

    @Inject
    public PreferredLanguageManager(@Nonnull ProjectId projectId,
                                    @Nonnull EventBus eventBus,
                                    @Nonnull DisplayLanguageStorage displayLanguageStorage) {
        this.projectId = checkNotNull(projectId);
        this.eventBus = checkNotNull(eventBus);
        this.displayLanguageStorage = checkNotNull(displayLanguageStorage);
    }

    @Nonnull
    public DisplayDictionaryLanguage getDisplayLanguage() {
        if(!loaded) {
            loaded = true;
            displayLanguage = displayLanguageStorage.get(DisplayDictionaryLanguage.empty());
            GWT.log("[PreferredLanguageManager] getDisplayLanguage: " + displayLanguage);
        }
        return displayLanguage;
    }

    public void setDisplayLanguage(@Nonnull DisplayDictionaryLanguage displayLanguage) {
        checkNotNull(displayLanguage);
        GWT.log("[PreferredLanguageManager] Setting display language (check): " + displayLanguage);
        if(this.displayLanguage.equals(displayLanguage)) {
            return;
        }
        GWT.log("[PreferredLanguageManager] Setting display language: " + displayLanguage);
        this.displayLanguage = checkNotNull(displayLanguage);
        this.displayLanguageStorage.store(displayLanguage);
        eventBus.fireEventFromSource(DisplayLanguageChangedEvent.get(projectId, displayLanguage).asGWTEvent(),
                                     projectId);
    }
}
