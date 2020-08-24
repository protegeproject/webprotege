package edu.stanford.bmir.protege.web.client.projectsettings;

import com.google.gwt.user.client.ui.AcceptsOneWidget;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-24
 */
public class ProjectSettingsHeaderSectionPresenter  {

    interface ProjectSettingsImportedHandler {
        void handleProjectSettingsImported();
    }

    @Nonnull
    private final ProjectSettingsHeaderSectionView view;

    @Nonnull
    private final ProjectSettingsDownloader projectSettingsDownloader;

    @Nonnull
    private final ProjectSettingsService projectSettingsService;

    @Nonnull
    private ProjectSettingsImportedHandler projectSettingsImportedHandler = () -> {};

    @Inject
    public ProjectSettingsHeaderSectionPresenter(@Nonnull ProjectSettingsHeaderSectionView view,
                                                 @Nonnull ProjectSettingsDownloader projectSettingsDownloader,
                                                 @Nonnull ProjectSettingsService projectSettingsService) {
        this.view = checkNotNull(view);
        this.projectSettingsDownloader = checkNotNull(projectSettingsDownloader);
        this.projectSettingsService = checkNotNull(projectSettingsService);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        view.setExportSettingsHandler(this::handleExportProjectSettings);
        view.setImportProjectSettingsHandler(this::handleImportProjectSettings);
    }

    public void setProjectSettingsImportedHandler(@Nonnull ProjectSettingsImportedHandler handler) {
        this.projectSettingsImportedHandler = checkNotNull(handler);
    }

    private void handleExportProjectSettings() {
        projectSettingsDownloader.download();
    }

    private void handleImportProjectSettings() {
        view.displayImportSettingsInputBox(this::handleImportSettings);
    }

    private void handleImportSettings(@Nonnull String settingsToImport) {
        projectSettingsService.importSettings(settingsToImport,
                                              this::handleSettingsImported,
                                              this::handleImportSettingsError);
    }

    private void handleSettingsImported() {
        this.projectSettingsImportedHandler.handleProjectSettingsImported();
    }

    private void handleImportSettingsError() {
        view.displayImportSettingsErrorMessage();
    }
}
