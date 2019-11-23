package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-18
 */
public class FormDescriptorViewImpl extends Composite implements FormDescriptorView {

    private AddFormElementHandler handler = () -> {};

    interface FormDescriptorViewImplUiBinder extends UiBinder<HTMLPanel, FormDescriptorViewImpl> {

    }

    private static FormDescriptorViewImplUiBinder ourUiBinder = GWT.create(FormDescriptorViewImplUiBinder.class);

    @UiField
    SimplePanel elementDescriptorListContainer;

    @UiField
    TextBox formIdField;

    @UiField(provided = true)
    LanguageMapEditor labelField;

    @UiField
    Button addElementButton;

    @Inject
    public FormDescriptorViewImpl(@Nonnull LanguageMapEditor labelEditor) {
        this.labelField = checkNotNull(labelEditor);
        initWidget(ourUiBinder.createAndBindUi(this));
        addElementButton.addClickHandler(this::handleAddElement);
    }

    private void handleAddElement(ClickEvent clickEvent) {
        handler.handleAddForm();
    }

    @Override
    public void setEnabled(boolean enabled) {
        formIdField.setEnabled(enabled);
        labelField.setEnabled(enabled);
        addElementButton.setEnabled(enabled);
    }

    @Nonnull
    @Override
    public String getFormId() {
        return formIdField.getText();
    }

    @Override
    public void setFormId(@Nonnull String formId) {
        formIdField.setText(formId);
    }

    @Nonnull
    @Override
    public LanguageMap getLabel() {
        return labelField.getValue().orElse(LanguageMap.empty());
    }

    @Override
    public void setLabel(@Nonnull LanguageMap label) {
        labelField.setValue(label);
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getElementDescriptorListContainer() {
        return elementDescriptorListContainer;
    }

    @Override
    public void setAddFormElementHandler(@Nonnull AddFormElementHandler handler) {
        this.handler = checkNotNull(handler);
    }
}
