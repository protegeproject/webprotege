package edu.stanford.bmir.protege.web.client.projectsettings;


import javax.annotation.Nonnull;
import java.util.function.Consumer;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-24
 */
public interface ProjectSettingsImporterView {

    void displayImportSettingsInputBox(@Nonnull Consumer<String> settingsToImportHandler);

    void displayImportSettingsErrorMessage();

    void displaySettingsImportedMessage();
}
