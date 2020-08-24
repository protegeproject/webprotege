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

    @Nonnull
    private final ProjectSettingsHeaderSectionView view;

    @Nonnull
    private final ProjectSettingsDownloader projectSettingsDownloader;

    @Inject
    public ProjectSettingsHeaderSectionPresenter(@Nonnull ProjectSettingsHeaderSectionView view,
                                                 @Nonnull ProjectSettingsDownloader projectSettingsDownloader) {
        this.view = checkNotNull(view);
        this.projectSettingsDownloader = checkNotNull(projectSettingsDownloader);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        view.setExportSettingsHandler(this::handleExportProjectSettings);
    }

    private void handleExportProjectSettings() {
        projectSettingsDownloader.download();
    }
}
