package edu.stanford.bmir.protege.web.client.perspective;

import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-03
 */
public interface PerspectivesManagerAdminSettingsView extends IsWidget {

    interface MakeDefaultTabsForProjectHandler {
        void handleMakeDefaultTabsForProject();
    }

    void setMakeDefaultTabsForProjectHandler(@Nonnull MakeDefaultTabsForProjectHandler handler);
}
