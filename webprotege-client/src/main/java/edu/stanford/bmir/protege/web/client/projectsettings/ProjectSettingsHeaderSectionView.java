package edu.stanford.bmir.protege.web.client.projectsettings;

import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-24
 */
public interface ProjectSettingsHeaderSectionView extends IsWidget {

    interface ExportSettingsHandler {
        void handleExportSettings();
    }

    void setExportSettingsHandler(@Nonnull ExportSettingsHandler handler);
}
