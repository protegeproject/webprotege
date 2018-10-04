package edu.stanford.bmir.protege.web.client.project;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.primitive.DefaultLanguageEditor;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.gwt.user.client.ui.FormPanel.ENCODING_MULTIPART;
import static com.google.gwt.user.client.ui.FormPanel.METHOD_POST;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 16 Nov 2017
 */
public class CreateNewProjectViewImpl extends Composite implements CreateNewProjectView {

    interface CreateNewProjectViewImplUiBinder extends UiBinder<HTMLPanel, CreateNewProjectViewImpl> {
    }

    private static CreateNewProjectViewImplUiBinder ourUiBinder = GWT.create(CreateNewProjectViewImplUiBinder.class);

    @UiField
    TextBox projectNameField;

    @UiField
    TextArea projectDescriptionField;

    @UiField
    FileUpload fileUpload;

    @UiField
    FormPanel formPanel;

    @UiField
    HTMLPanel fileUploadArea;

    @UiField(provided = true)
    DefaultLanguageEditor projectLanguageField;

    @Nonnull
    private final MessageBox messageBox;

    private HandlerRegistration submitCompleteHandlerRegistraion = () -> {};


    @Inject
    public CreateNewProjectViewImpl(@Nonnull DefaultLanguageEditor languageEditor,
                                    @Nonnull MessageBox messageBox) {
        this.projectLanguageField = checkNotNull(languageEditor);
        this.messageBox = messageBox;
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Nonnull
    @Override
    public String getProjectName() {
        return projectNameField.getText().trim();
    }

    @Nonnull
    @Override
    public String getProjectDescription() {
        return projectDescriptionField.getText().trim();
    }

    @Nonnull
    @Override
    public String getProjectLanguage() {
        return projectLanguageField.getValue().orElse("").trim();
    }

    @Override
    public void setFileUploadEnabled(boolean enabled) {
        fileUpload.setEnabled(enabled);
        fileUploadArea.setVisible(enabled);
    }

    @Override
    public void setFileUploadPostUrl(@Nonnull String url) {
        fileUpload.setName("file");
        formPanel.setMethod(METHOD_POST);
        formPanel.setEncoding(ENCODING_MULTIPART);
        formPanel.setAction(checkNotNull(url));
    }

    @Override
    public boolean isFileUploadSpecified() {
        String filename = fileUpload.getFilename();
        return !filename.trim().isEmpty();
    }

    @Override
    public void setSubmitCompleteHandler(@Nonnull FormPanel.SubmitCompleteHandler handler) {
        submitCompleteHandlerRegistraion.removeHandler();
        submitCompleteHandlerRegistraion = formPanel.addSubmitCompleteHandler(handler);
    }

    @Override
    public void submitFormData() {
        formPanel.submit();
    }

    @Override
    public void showProjectNameMissingMessage() {
        messageBox.showAlert("Project name missing", "Please enter a project name");
    }

    @Override
    public void clear() {
        projectNameField.setText("");
        projectDescriptionField.setText("");
    }

    @Override
    public Optional<HasRequestFocus> getInitialFocusable() {
        return Optional.of(() -> projectNameField.setFocus(true));
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        projectNameField.setFocus(true);
    }
}