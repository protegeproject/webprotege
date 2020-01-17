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

    private AddFormHandler addFormHandler = () -> {};

    private FormSelectedHandler formSelectedHandler = (formId) -> {};

    interface FormsManagerViewImplUiBinder extends UiBinder<HTMLPanel, FormsManagerViewImpl> {

    }

    private static FormsManagerViewImplUiBinder ourUiBinder = GWT.create(FormsManagerViewImplUiBinder.class);

    @UiField
    Button addFormButton;



    @Inject
    public FormsManagerViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        addFormButton.addClickHandler(this::handleAddForm);
    }

    @Override
    public void setAddFormHandler(@Nonnull AddFormHandler handler) {
        this.addFormHandler = checkNotNull(handler);
    }

    @Override
    public void setFormSelectedHandler(@Nonnull FormSelectedHandler handler) {
        this.formSelectedHandler = checkNotNull(handler);
    }

    @Override
    public void clear() {
    }

    private void handleAddForm(ClickEvent clickEvent) {
        addFormHandler.handleAddForm();
    }
}
