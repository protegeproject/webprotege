package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.library.msgbox.InputBox;
import edu.stanford.bmir.protege.web.client.library.msgbox.InputBoxHandler;
import edu.stanford.bmir.protege.web.shared.form.FormId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-17
 */
public class FormsManagerViewImpl extends Composite implements FormsManagerView {

    @Nonnull
    private final InputBox inputBox;

    private AddFormHandler addFormHandler = () -> {};

    private FormSelectionChangedHandler formSelectionChangedHandler = () -> {};

    interface FormsManagerViewImplUiBinder extends UiBinder<HTMLPanel, FormsManagerViewImpl> {

    }

    private static FormsManagerViewImplUiBinder ourUiBinder = GWT.create(FormsManagerViewImplUiBinder.class);

    @UiField
    SimplePanel formDescriptorContainer;

    @UiField
    ListBox formSelector;

    @UiField
    Button addFormButton;

    @Inject
    public FormsManagerViewImpl(@Nonnull InputBox inputBox) {
        this.inputBox = checkNotNull(inputBox);
        initWidget(ourUiBinder.createAndBindUi(this));
        addFormButton.addClickHandler(this::handleAddForm);
        formSelector.addChangeHandler(event -> formSelectionChangedHandler.handleFormSelectionChanged());
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getFormDescriptorContainer() {
        return formDescriptorContainer;
    }

    @Override
    public void displayCreateFormIdPrompt(FormIdEnteredHandler handler) {
        inputBox.showDialog("Enter form Id",
                            false,
                            "",
                            handler::handleAcceptFormName);
    }

    @Nonnull
    @Override
    public Optional<FormId> getCurrentFormId() {
        return Optional.ofNullable(formSelector.getSelectedValue())
                .map(FormId::get);
    }

    @Override
    public void setFormSelectionChangedHandler(@Nonnull FormSelectionChangedHandler handler) {
        this.formSelectionChangedHandler = checkNotNull(handler);
    }

    @Override
    public void setAddFormHandler(@Nonnull AddFormHandler handler) {
        this.addFormHandler = checkNotNull(handler);
    }

    @Override
    public void clear() {
    }

    private void handleAddForm(ClickEvent clickEvent) {
        addFormHandler.handleAddForm();
    }

    @Override
    public void setFormIds(@Nonnull List<FormId> formIds) {
        formSelector.clear();
        formIds.stream()
               .map(FormId::getId)
               .forEach(formSelector::addItem);
        if(formSelector.getItemCount() > 0) {
            formSelector.setSelectedIndex(0);
        }
    }

    @Override
    public void addFormId(@Nonnull FormId formId) {
        formSelector.addItem(formId.getId());
    }

    @Override
    public void setCurrentFormId(@Nonnull FormId formId) {
        for(int i = 0; i < formSelector.getItemCount(); i++) {
            if(formSelector.getValue(i).equals(formId.getId())) {
                formSelector.setSelectedIndex(i);
                return;
            }
        }
    }
}
