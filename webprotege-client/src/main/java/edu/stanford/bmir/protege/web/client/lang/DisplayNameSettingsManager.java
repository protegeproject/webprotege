package edu.stanford.bmir.protege.web.client.lang;

import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.project.ActiveProjectManager;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.lang.DisplayNameSettings;
import edu.stanford.bmir.protege.web.shared.lang.DisplayNameSettingsChangedEvent;
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
public class DisplayNameSettingsManager {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final EventBus eventBus;

    @Nonnull
    private final DisplayLanguageStorage displayLanguageStorage;

    @Nonnull
    private final ActiveProjectManager activeProjectManager;

    @Nonnull
    private DisplayNameSettings displayNameSettings = DisplayNameSettings.empty();

    private boolean loaded = false;

    @Inject
    public DisplayNameSettingsManager(@Nonnull ProjectId projectId,
                                      @Nonnull EventBus eventBus,
                                      @Nonnull DisplayLanguageStorage displayLanguageStorage,
                                      @Nonnull ActiveProjectManager activeProjectManager) {
        this.projectId = checkNotNull(projectId);
        this.eventBus = checkNotNull(eventBus);
        this.displayLanguageStorage = checkNotNull(displayLanguageStorage);
        this.activeProjectManager = checkNotNull(activeProjectManager);
    }

    @Nonnull
    public DisplayNameSettings getLocalDisplayNameSettings() {
        if(!loaded) {
            loaded = true;
            displayNameSettings = displayLanguageStorage.get(DisplayNameSettings.empty());
        }
        return displayNameSettings;
    }

    public void setLocalDisplayNameSettings(@Nonnull DisplayNameSettings displayNameSettings) {
        checkNotNull(displayNameSettings);
        if(this.displayNameSettings.equals(displayNameSettings)) {
            return;
        }
        this.displayNameSettings = checkNotNull(displayNameSettings);
        this.displayLanguageStorage.store(displayNameSettings);
        eventBus.fireEventFromSource(DisplayNameSettingsChangedEvent.get(projectId, displayNameSettings).asGWTEvent(),
                                     projectId);
    }
}
