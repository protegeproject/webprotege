package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public class FormElementViewImpl extends Composite implements FormElementView {

    interface FormElementViewImplUiBinder extends UiBinder<HTMLPanel, FormElementViewImpl> {

    }

    private static FormElementViewImplUiBinder ourUiBinder = GWT.create(FormElementViewImplUiBinder.class);

    @UiField
    Label label;

    @UiField
    SimplePanel editorHolder;

    public FormElementViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
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