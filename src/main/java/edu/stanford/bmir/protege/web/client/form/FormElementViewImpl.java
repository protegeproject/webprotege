package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import edu.stanford.bmir.protege.web.shared.form.field.FormElementDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.FormElementId;
import edu.stanford.bmir.protege.web.shared.form.field.Required;

import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public class FormElementViewImpl extends Composite implements FormElementView {

    interface FormElementViewImplUiBinder extends UiBinder<HTMLPanel, FormElementViewImpl> {

    }

    private static FormElementViewImplUiBinder ourUiBinder = GWT.create(FormElementViewImplUiBinder.class);

    private FormElementId formElementId = null;

    private Required required = Required.OPTIONAL;

    @UiField
    Label label;

    @UiField
    SimplePanel editorHolder;

    @Inject
    public FormElementViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setId(FormElementId elementId) {
        formElementId = elementId;
    }

    @Override
    public void setRequired(Required required) {
        this.required = checkNotNull(required);
    }

    @Override
    public Required getRequired() {
        return required;
    }

    @Override
    public void setRequiredValueNotPresentVisible(boolean visible) {
        if(visible) {
            label.getElement().getStyle().setColor("#ff0000");
        }
        else {
            label.getElement().getStyle().clearColor();
        }
    }

    @Override
    public Optional<FormElementId> getId() {
        return Optional.ofNullable(formElementId);
    }

    @Override
    public void setFormLabel(String formLabel) {
        label.setText(formLabel);
    }

    @Override
    public void setEditor(FormElementEditor editor) {
        editorHolder.setWidget(editor);
    }

    @Override
    public FormElementEditor getEditor() {
        return (FormElementEditor) editorHolder.getWidget();
    }
}