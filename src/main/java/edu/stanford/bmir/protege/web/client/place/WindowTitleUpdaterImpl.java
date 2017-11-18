package edu.stanford.bmir.protege.web.client.place;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.project.ActiveProjectChangedEvent;
import edu.stanford.bmir.protege.web.client.project.ActiveProjectManager;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.client.project.ActiveProjectChangedEvent.ACTIVE_PROJECT_CHANGED;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 15 Nov 2017
 */
@ApplicationSingleton
public class WindowTitleUpdaterImpl implements WindowTitleUpdater {

    @Nonnull
    private final ActiveProjectManager activeProjectManager;

    @Nonnull
    private final EventBus eventBus;

    private String defaultWindowTitle = "";

    @Inject
    public WindowTitleUpdaterImpl(@Nonnull ActiveProjectManager activeProjectManager,
                                  @Nonnull EventBus eventBus) {
        this.activeProjectManager = checkNotNull(activeProjectManager);
        this.eventBus = checkNotNull(eventBus);
    }

    public void start() {
        this.defaultWindowTitle = Window.getTitle();
        GWT.log("[WindowTitleUpdaterImpl] Starting WindowTitleUpdaterImpl");
        eventBus.addHandler(ACTIVE_PROJECT_CHANGED, this::updateWindowTitle);
    }

    private void updateWindowTitle(ActiveProjectChangedEvent event) {
        GWT.log("[WindowTitleUpdaterImpl] Active project changed");
        activeProjectManager.getActiveProjectDetails(projectDetails -> {
            GWT.log("[WindowTitleUpdaterImpl] Active project details: " + projectDetails);
            String windowTitle;
            windowTitle = projectDetails.map(details -> details.getDisplayName() + " – " + defaultWindowTitle)
                                        .orElse(defaultWindowTitle);
            Window.setTitle(windowTitle);
        });
    }
}
