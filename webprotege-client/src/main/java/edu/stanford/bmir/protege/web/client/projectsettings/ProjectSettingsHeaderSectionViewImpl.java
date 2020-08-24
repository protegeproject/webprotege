package edu.stanford.bmir.protege.web.client.projectsettings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

public class ProjectSettingsHeaderSectionViewImpl extends Composite implements ProjectSettingsHeaderSectionView {

    private ExportSettingsHandler exportSettingsHandler = () -> {};

    interface ProjectSettingsHeaderSectionViewImplUiBinder extends UiBinder<HTMLPanel, ProjectSettingsHeaderSectionViewImpl> {
    }

    private static ProjectSettingsHeaderSectionViewImplUiBinder ourUiBinder = GWT.create(
            ProjectSettingsHeaderSectionViewImplUiBinder.class);

    @UiField
    Button exportButton;

    @Inject
    public ProjectSettingsHeaderSectionViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        exportButton.addClickHandler(this::handleExportSettingsButtonClicked);
    }

    private void handleExportSettingsButtonClicked(ClickEvent event) {
        exportSettingsHandler.handleExportSettings();
    }

    @Override
    public void setExportSettingsHandler(@Nonnull ExportSettingsHandler handler) {
        this.exportSettingsHandler = checkNotNull(handler);
    }

}