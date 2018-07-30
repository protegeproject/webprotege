package edu.stanford.bmir.protege.web.shared.lang;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
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
    private String prefLang = "";

    @Inject
    public PreferredLanguageManager(@Nonnull ProjectId projectId, @Nonnull EventBus eventBus) {
        this.projectId = checkNotNull(projectId);
        this.eventBus = checkNotNull(eventBus);
    }

    @Nonnull
    public String getPrefLang() {
        return prefLang;
    }

    public void setPrefLang(@Nonnull String prefLang) {
        GWT.log("[PreferredLanguageManager] setPrefLang: " + prefLang);
        if (prefLang.equals(this.prefLang)) {
            return;
        }
        this.prefLang = checkNotNull(prefLang);
        PrefLangChangedEvent event = PrefLangChangedEvent.get(projectId, prefLang);
        eventBus.fireEventFromSource(event.asGWTEvent(), projectId);
    }
}
