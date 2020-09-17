package edu.stanford.bmir.protege.web.client.projectsettings;

import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.library.msgbox.InputBox;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-24
 */
public class ProjectSettingsImporterViewImpl implements ProjectSettingsImporterView {

    @Nonnull
    private final Messages messages;

    @Nonnull
    private final InputBox inputBox;

    @Nonnull
    private final MessageBox messageBox;

    @Inject
    public ProjectSettingsImporterViewImpl(@Nonnull Messages messages,
                                           @Nonnull InputBox inputBox,
                                           @Nonnull MessageBox messageBox) {
        this.messages = checkNotNull(messages);
        this.inputBox = checkNotNull(inputBox);
        this.messageBox = checkNotNull(messageBox);
    }

    @Override
    public void displayImportSettingsInputBox(@Nonnull Consumer<String> settingsToImportHandler) {
        inputBox.showDialog(messages.projectSettings_importSettings_title(),
                            messages.projectSettings_importSettings_message(),
                            true, "",
                            settingsToImportHandler::accept);
    }

    @Override
    public void displayImportSettingsErrorMessage() {
        messageBox.showAlert(messages.projectSettings_importSettings_error_title(),
                             messages.projectSettings_importSettings_error_message());
    }

    @Override
    public void displaySettingsImportedMessage() {
        messageBox.showAlert(messages.settings_importSettings_complete_title(),
                             messages.settings_importSettings_complete_message());
    }
}
