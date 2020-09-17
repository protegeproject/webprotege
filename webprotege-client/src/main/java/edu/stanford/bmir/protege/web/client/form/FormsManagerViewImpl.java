package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.FormsMessages;
import edu.stanford.bmir.protege.web.client.library.msgbox.InputBox;
import edu.stanford.bmir.protege.web.client.library.msgbox.InputBoxHandler;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-17
 */
public class FormsManagerViewImpl extends Composite implements FormsManagerView {

    private CopyFormsFromProjectHandler copyFormsFromProjectHandler = () -> {};

    @UiField
    protected SimplePanel formsListContainer;

    private ExportFormsHandler exportFormsHandler = () -> {};

    private ImportFormsHandler importFormsHandler = () -> {};

    interface FormsManagerViewImplUiBinder extends UiBinder<HTMLPanel, FormsManagerViewImpl> {

    }

    private static FormsManagerViewImplUiBinder ourUiBinder = GWT.create(FormsManagerViewImplUiBinder.class);

    @UiField
    Button copyFormsFromProjectButton;

    @UiField
    Button exportFormsFromProjectButton;
    @UiField
    Button importFormsButton;

    @Nonnull
    private final MessageBox messageBox;

    @Nonnull
    private final FormsMessages formsMessages;

    private InputBox inputBox;

    @Inject
    public FormsManagerViewImpl(@Nonnull MessageBox messageBox,
                                @Nonnull FormsMessages formsMessages,
                                @Nonnull InputBox inputBox) {
        this.messageBox = messageBox;
        this.formsMessages = formsMessages;
        this.inputBox = inputBox;
        initWidget(ourUiBinder.createAndBindUi(this));
        copyFormsFromProjectButton.addClickHandler(this::handleCopyFormsFromProject);
        exportFormsFromProjectButton.addClickHandler(this::handleExportForms);
        importFormsButton.addClickHandler(this::handleImportForms);
    }

    private void handleImportForms(ClickEvent event) {
        importFormsHandler.handleImportForms();
    }

    private void handleExportForms(ClickEvent event) {
        exportFormsHandler.handleExportForms();
    }


    @Override
    public void clear() {
    }

    @Override
    public void displayImportFormsInputBox(Consumer<String> importFormsJson) {
        inputBox.showDialog(formsMessages.importFormsIntoProject_title(),
                            formsMessages.importFormsIntoProject_message(),
                            true, "", importFormsJson::accept);
    }

    @Override
    public void displayImportFormsErrorMessage() {
        messageBox.showAlert(formsMessages.importFormsIntoProject_error_title(),
                             formsMessages.importFormsIntoProject_error_message());
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getFormsListContainer() {
        return formsListContainer;
    }


    private void handleCopyFormsFromProject(ClickEvent event) {
        copyFormsFromProjectHandler.handleCopyFromsFromProject();
    }

    @Override
    public void setCopyFormsFromProjectHandler(@Nonnull CopyFormsFromProjectHandler handler) {
        this.copyFormsFromProjectHandler = checkNotNull(handler);
    }

    @Override
    public void setExportFormsHandler(@Nonnull ExportFormsHandler exportFormsHandler) {
        this.exportFormsHandler = checkNotNull(exportFormsHandler);
    }

    @Override
    public void setImportFormsHandler(@Nonnull ImportFormsHandler importFormsHandler) {
        this.importFormsHandler = checkNotNull(importFormsHandler);
    }
}
