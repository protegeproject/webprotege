package edu.stanford.bmir.protege.web.client.lang;

import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.lang.DisplayNameSettings;
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
    private DisplayNameSettings displayNameSettings = DisplayNameSettings.empty();

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
    public DisplayNameSettings getDisplayLanguage() {
        if(!loaded) {
            loaded = true;
            displayNameSettings = displayLanguageStorage.get(DisplayNameSettings.empty());
        }
        return displayNameSettings;
    }

    public void setDisplayLanguage(@Nonnull DisplayNameSettings displayNameSettings) {
        checkNotNull(displayNameSettings);
        if(this.displayNameSettings.equals(displayNameSettings)) {
            return;
        }
        this.displayNameSettings = checkNotNull(displayNameSettings);
        this.displayLanguageStorage.store(displayNameSettings);
        eventBus.fireEventFromSource(DisplayLanguageChangedEvent.get(projectId, displayNameSettings).asGWTEvent(),
                                     projectId);
    }
}
