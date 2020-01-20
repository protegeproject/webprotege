package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.FormsMessages;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.list.ListBox;
import edu.stanford.bmir.protege.web.shared.form.FormId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-17
 */
public class FormsManagerViewImpl extends Composite implements FormsManagerView {

    private AddFormHandler addFormHandler = () -> {};

    private DeleteFormHandler deleteFormHandler = (formId) -> {};

    private EditFormHandler editFormHandler = (formId) -> {};

    private FormSelectedHandler formSelectedHandler = (formId) -> {};

    interface FormsManagerViewImplUiBinder extends UiBinder<HTMLPanel, FormsManagerViewImpl> {

    }

    private static FormsManagerViewImplUiBinder ourUiBinder = GWT.create(FormsManagerViewImplUiBinder.class);

    @UiField
    Button addFormButton;

    @UiField
    ListBox<FormId, FormIdPresenter> formListField;

    @UiField
    Button editFormButton;

    @UiField
    Button deleteFormButton;

    @Nonnull
    private final MessageBox messageBox;

    @Nonnull
    private final FormsMessages formsMessages;

    @Inject
    public FormsManagerViewImpl(@Nonnull MessageBox messageBox,
                                @Nonnull FormsMessages formsMessages) {
        this.messageBox = messageBox;
        this.formsMessages = formsMessages;
        initWidget(ourUiBinder.createAndBindUi(this));
        addFormButton.addClickHandler(this::handleAddForm);
        deleteFormButton.addClickHandler(this::handleDeleteForm);
        editFormButton.addClickHandler(this::handleEditForm);
        formListField.setRenderer(FormIdPresenter::getView);
        formListField.addSelectionHandler(this::handleSelection);
        deleteFormButton.setEnabled(false);
        editFormButton.setEnabled(false);
    }

    @Override
    public void setAddFormHandler(@Nonnull AddFormHandler handler) {
        this.addFormHandler = checkNotNull(handler);
    }

    @Override
    public void setDeleteFormHandler(@Nonnull DeleteFormHandler handler) {
        this.deleteFormHandler = checkNotNull(handler);
    }

    @Override
    public void setEditFormHandler(@Nonnull EditFormHandler handler) {
        this.editFormHandler = checkNotNull(handler);
    }

    @Override
    public void setFormSelectedHandler(@Nonnull FormSelectedHandler handler) {
        this.formSelectedHandler = checkNotNull(handler);
    }

    @Override
    public void clear() {
        formListField.setListData(Collections.emptyList());
    }

    @Override
    public void setForms(List<FormIdPresenter> forms) {
        formListField.setListData(forms);
    }

    @Override
    public void displayDeleteFormConfirmationMessage(@Nonnull String displayName,
                                                     @Nonnull FormId formId,
                                                     @Nonnull DeleteFormCallback deleteFormCallback) {
        messageBox.showConfirmBox("Delete form?",
                                  formsMessages.deleteFormElementConfirmation_Message(displayName),
                                  DialogButton.CANCEL,
                                  DialogButton.DELETE,
                                  () -> deleteFormCallback.deleteForm(formId),
                                  DialogButton.CANCEL);
    }

    private void handleAddForm(ClickEvent clickEvent) {
        addFormHandler.handleAddForm();
    }


    private void handleSelection(SelectionEvent<List<FormIdPresenter>> event) {
        boolean selectionNonEmpty = !event.getSelectedItem()
                                .isEmpty();
        deleteFormButton.setEnabled(selectionNonEmpty);
        editFormButton.setEnabled(selectionNonEmpty);
        formListField.getFirstSelectedElement()
                     .ifPresent(formIdPresenter -> formSelectedHandler.handleFormSelectionChanged(formIdPresenter.getFormId()));
    }

    private void handleDeleteForm(ClickEvent event) {
        formListField.getFirstSelectedElement().ifPresent(f -> {
            f.getFormId().ifPresent(fid -> deleteFormHandler.handleDeleteForm(fid));
        });
    }

    private void handleEditForm(ClickEvent event) {
        formListField.getFirstSelectedElement().ifPresent(f -> {
            f.getFormId().ifPresent(fid -> editFormHandler.handleEditForm(fid));
        });
    }

}
