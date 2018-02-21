package edu.stanford.bmir.protege.web.client.project;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;

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

    private HandlerRegistration submitCompleteHandlerRegistraion = () -> {};


    @Inject
    public CreateNewProjectViewImpl() {
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
    public void clear() {
        projectNameField.setText("");
        projectDescriptionField.setText("");
    }

    @Override
    public Optional<HasRequestFocus> getInitialFocusable() {
        return Optional.of(() -> projectNameField.setFocus(true));
    }
}