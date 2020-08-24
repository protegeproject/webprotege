package edu.stanford.bmir.protege.web.client.projectsettings;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-24
 */
public class ProjectSettingsImporter {

    @Nonnull
    private final ProjectSettingsImporterView view;

    @Nonnull
    private final ProjectSettingsService projectSettingsService;

    @Inject
    public ProjectSettingsImporter(@Nonnull ProjectSettingsImporterView view,
                                   @Nonnull ProjectSettingsService projectSettingsService) {
        this.view = checkNotNull(view);
        this.projectSettingsService = checkNotNull(projectSettingsService);
    }

    public void importSettings() {
        view.displayImportSettingsInputBox(this::handleImportSettings);
    }

    private void handleImportSettings(String settingsJson) {
        projectSettingsService.importSettings(settingsJson,
                                              this::handleSettingsImported,
                                              this::handleImportSettingsError);
    }

    private void handleImportSettingsError() {
        view.displayImportSettingsErrorMessage();
    }

    private void handleSettingsImported() {
        view.displaySettingsImportedMessage();
    }
}
