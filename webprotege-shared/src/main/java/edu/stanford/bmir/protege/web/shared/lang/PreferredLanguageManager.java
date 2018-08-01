package edu.stanford.bmir.protege.web.shared.lang;

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
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
    private DisplayDictionaryLanguage displayLanguage = DisplayDictionaryLanguage.empty();

    @Inject
    public PreferredLanguageManager(@Nonnull ProjectId projectId, @Nonnull EventBus eventBus) {
        this.projectId = checkNotNull(projectId);
        this.eventBus = checkNotNull(eventBus);
    }

    @Nonnull
    public DisplayDictionaryLanguage getDisplayLanguage() {
        return displayLanguage;
    }

    public void setDisplayLanguage(@Nonnull DisplayDictionaryLanguage displayLanguage) {
        checkNotNull(displayLanguage);
        if(this.displayLanguage.equals(displayLanguage)) {
            return;
        }
        GWT.log("Setting display language: " + displayLanguage);
        this.displayLanguage = checkNotNull(displayLanguage);
        eventBus.fireEventFromSource(DisplayLanguageChangedEvent.get(projectId, displayLanguage).asGWTEvent(),
                                     projectId);
    }
}
