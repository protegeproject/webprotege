package edu.stanford.bmir.protege.web.client.projectsettings;

import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-24
 */
public interface ProjectSettingsHeaderSectionView extends IsWidget {


    interface ExportSettingsHandler {
        void handleExportSettings();
    }

    interface ImportProjectSettingsHandler {
        void handleImportSettings();
    }

    void setExportSettingsHandler(@Nonnull ExportSettingsHandler handler);

    void setImportProjectSettingsHandler(@Nonnull ImportProjectSettingsHandler handler);

    void displayImportSettingsInputBox(@Nonnull Consumer<String> settingsImportedHandler);

    void displayImportSettingsErrorMessage();
}
