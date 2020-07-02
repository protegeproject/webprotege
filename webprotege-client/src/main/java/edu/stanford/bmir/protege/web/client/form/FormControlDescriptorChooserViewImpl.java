package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-26
 */
public class FormControlDescriptorChooserViewImpl extends Composite implements FormControlDescriptorChooserView {

    private Runnable fieldTypeChangedHander = () -> {};

    @UiField
    ListBox typesComboBox;

    @UiField
    SimplePanel fieldEditorContainer;

    interface FormControlDescriptorChooserViewImplUiBinder extends UiBinder<HTMLPanel, FormControlDescriptorChooserViewImpl> {

    }

    private static FormControlDescriptorChooserViewImplUiBinder ourUiBinder = GWT.create(
            FormControlDescriptorChooserViewImplUiBinder.class);

    @Inject
    public FormControlDescriptorChooserViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        typesComboBox.addChangeHandler(event -> fieldTypeChangedHander.run());
    }

    @Nonnull
    @Override
    public String getFieldType() {
        return typesComboBox.getSelectedValue();
    }

    @Override
    public void setFieldType(@Nonnull String fieldType) {
        for(int i = 0; i < typesComboBox.getItemCount(); i++) {
            if(typesComboBox.getValue(i).equals(fieldType)) {
                typesComboBox.setSelectedIndex(i);
                return;
            }
        }
    }

    @Override
    public void addAvailableFieldType(@Nonnull String value,
                                      @Nonnull String label) {
        typesComboBox.addItem(label, value);
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getFieldEditorContainer() {
        return fieldEditorContainer;
    }

    @Override
    public void setFieldTypeChangedHandler(Runnable handler) {
        this.fieldTypeChangedHander = checkNotNull(handler);
    }
}
