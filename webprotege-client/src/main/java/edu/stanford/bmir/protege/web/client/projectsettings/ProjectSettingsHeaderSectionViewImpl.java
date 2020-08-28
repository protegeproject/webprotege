package edu.stanford.bmir.protege.web.client.projectsettings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.library.msgbox.InputBox;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

public class ProjectSettingsHeaderSectionViewImpl extends Composite implements ProjectSettingsHeaderSectionView {

    public static final String NO_INITIAL_INPUT_TEXT = "";
    @Nonnull
    private ExportSettingsHandler exportSettingsHandler = () -> {};

    @Nonnull
    private ImportProjectSettingsHandler importProjectSettingsHandler = () -> {};
    private InputBox inputBox;
    private Messages messages;
    private MessageBox messageBox;

    interface ProjectSettingsHeaderSectionViewImplUiBinder extends UiBinder<HTMLPanel, ProjectSettingsHeaderSectionViewImpl> {
    }

    private static ProjectSettingsHeaderSectionViewImplUiBinder ourUiBinder = GWT.create(
            ProjectSettingsHeaderSectionViewImplUiBinder.class);

    @UiField
    Button exportButton;

    @UiField
    Button importButton;

    @Inject
    public ProjectSettingsHeaderSectionViewImpl(InputBox inputBox,
                                                Messages messages,
                                                MessageBox messageBox) {
        this.inputBox = checkNotNull(inputBox);
        this.messages = checkNotNull(messages);
        this.messageBox = checkNotNull(messageBox);
        initWidget(ourUiBinder.createAndBindUi(this));
        exportButton.addClickHandler(this::handleExportSettingsButtonClicked);
        importButton.addClickHandler(this::handleImportSettingsButtonClicked);
    }

    private void handleImportSettingsButtonClicked(ClickEvent event) {
        importProjectSettingsHandler.handleImportSettings();
    }

    private void handleExportSettingsButtonClicked(ClickEvent event) {
        exportSettingsHandler.handleExportSettings();
    }

    @Override
    public void setExportSettingsHandler(@Nonnull ExportSettingsHandler handler) {
        this.exportSettingsHandler = checkNotNull(handler);
    }

    @Override
    public void setImportProjectSettingsHandler(@Nonnull ImportProjectSettingsHandler handler) {
        this.importProjectSettingsHandler = checkNotNull(handler);
    }

    @Override
    public void displayImportSettingsInputBox(@Nonnull Consumer<String> settingsToImportHandler) {
        inputBox.showDialog(messages.projectSettings_importSettings_title(),
                            messages.projectSettings_importSettings_message(),
                            true, NO_INITIAL_INPUT_TEXT,
                            settingsToImportHandler::accept);
    }

    @Override
    public void displayImportSettingsErrorMessage() {
        messageBox.showAlert(messages.projectSettings_importSettings_error_title(),
                             messages.projectSettings_importSettings_error_message());
    }


}