package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.ui.Counter;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnId;
import edu.stanford.bmir.protege.web.shared.form.field.Optionality;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-26
 */
public class GridColumnDescriptorViewImpl extends Composite implements GridColumnDescriptorView {

    interface GridColumnDescriptorViewImplUiBinder extends UiBinder<HTMLPanel, GridColumnDescriptorViewImpl> {

    }

    private static GridColumnDescriptorViewImplUiBinder ourUiBinder = GWT.create(GridColumnDescriptorViewImplUiBinder.class);

    @UiField
    TextBox idField;

    @UiField(provided = true)
    LanguageMapEditor labelField;

    @UiField
    SimplePanel fieldDescriptorChooserContainer;

    @UiField
    SimplePanel bindingViewContainer;

    @UiField(provided = true)
    static Counter counter = new Counter();

    @UiField
    RadioButton requiredRadio;

    @UiField
    RadioButton optionalRadio;

    @Inject
    public GridColumnDescriptorViewImpl(LanguageMapEditor labelField) {
        this.labelField = labelField;
        initWidget(ourUiBinder.createAndBindUi(this));
        counter.increment();
    }

    @Override
    public void setId(@Nonnull GridColumnId id) {
        idField.setText(id.getId());
    }

    @Nonnull
    @Override
    public GridColumnId getId() {
        return GridColumnId.get(idField.getText().trim());
    }

    @Override
    public void setOptionality(Optionality optionality) {
        if(optionality == Optionality.REQUIRED) {
            requiredRadio.setValue(true);
        }
        else {
            optionalRadio.setValue(true);
        }
    }

    @Override
    public Optionality getOptionality() {
        if(requiredRadio.getValue()) {
            return Optionality.REQUIRED;
        }
        else {
            return Optionality.OPTIONAL;
        }
    }

    @Override
    public void setLabel(@Nonnull LanguageMap label) {
        labelField.setValue(label);
    }

    @Nonnull
    @Override
    public LanguageMap getLabel() {
        return labelField.getValue().orElse(LanguageMap.empty());
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getBindingViewContainer() {
        return bindingViewContainer;
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getFieldDescriptorChooserContainer() {
        return fieldDescriptorChooserContainer;
    }
}
