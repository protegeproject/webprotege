package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditor;
import edu.stanford.bmir.protege.web.client.ui.Counter;
import edu.stanford.bmir.protege.web.shared.entity.OWLPropertyData;
import edu.stanford.bmir.protege.web.shared.form.ExpansionState;
import edu.stanford.bmir.protege.web.shared.form.field.FieldRun;
import edu.stanford.bmir.protege.web.shared.form.field.FormFieldId;
import edu.stanford.bmir.protege.web.shared.form.field.Optionality;
import edu.stanford.bmir.protege.web.shared.form.field.Repeatability;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-16
 */
public class FormFieldDescriptorViewImpl extends Composite implements FormFieldDescriptorView {


    private Consumer<LanguageMap> labelChangedHander = label -> {};

    interface FormFieldDescriptorEditorViewImplUiBinder extends UiBinder<HTMLPanel, FormFieldDescriptorViewImpl> {

    }

    private static FormFieldDescriptorEditorViewImplUiBinder ourUiBinder = GWT.create(
            FormFieldDescriptorEditorViewImplUiBinder.class);

    @UiField
    RadioButton optionalRadio;

    @UiField
    RadioButton requiredRadio;

    @UiField(provided = true)
    LanguageMapEditor labelEditor;

    @UiField(provided = true)
    LanguageMapEditor helpEditor;


    @UiField
    RepeatabilityView repeatabilityView;

    @UiField
    RadioButton elementRunStartRadio;

    @UiField
    RadioButton elementRunContinueRadio;

    @UiField(provided = true)
    protected static Counter counter = new Counter();

    @UiField
    SimplePanel fieldViewContainer;

    @UiField
    SimplePanel bindingViewContainer;
    @UiField
    CheckBox readOnlyCheckBox;
    @UiField
    RadioButton initialExpansionStateExpanded;
    @UiField
    RadioButton initialExpansionStateCollapsed;

    @Inject
    public FormFieldDescriptorViewImpl(LanguageMapEditor labelEditor,
                                       LanguageMapEditor helpEditor) {
        counter.increment();
        this.labelEditor = labelEditor;
        this.helpEditor = helpEditor;
        initWidget(ourUiBinder.createAndBindUi(this));
        labelEditor.addValueChangeHandler(event -> {
            labelChangedHander.accept(getLabel());
        });
    }

    @Override
    public void requestFocus() {

    }

    @Override
    public void setHelp(@Nonnull LanguageMap help) {
        helpEditor.setValue(help);
    }

    @Nonnull
    @Override
    public LanguageMap getHelp() {
        return helpEditor.getValue().orElse(LanguageMap.empty());
    }

    @Override
    public void setLabel(@Nonnull LanguageMap label) {
        labelEditor.setValue(label);
    }

    @Nonnull
    @Override
    public LanguageMap getLabel() {
        return labelEditor.getValue().orElse(LanguageMap.empty());
    }

    @Override
    public void setFieldRun(@Nonnull FieldRun fieldRun) {
        if(fieldRun == FieldRun.CONTINUE) {
            elementRunContinueRadio.setValue(true);
        }
        else {
            elementRunStartRadio.setValue(true);
        }
    }

    @Nonnull
    @Override
    public FieldRun getFieldRun() {
        if(elementRunContinueRadio.getValue()) {
            return FieldRun.CONTINUE;
        }
        else {
            return FieldRun.START;
        }
    }

    @Override
    public void setOptionality(@Nonnull Optionality optionality) {
        GWT.log("[FormFieldDescriptorViewImpl] setOptionality");
        if(optionality == Optionality.OPTIONAL) {
            optionalRadio.setValue(true);
        }
        else {
            requiredRadio.setValue(true);
        }
    }

    @Nonnull
    @Override
    public Optionality getOptionality() {
        return optionalRadio.getValue() ? Optionality.OPTIONAL : Optionality.REQUIRED;
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getOwlBindingViewContainer() {
        return bindingViewContainer;
    }

    @Override
    public void setRepeatability(@Nonnull Repeatability repeatability) {
        repeatabilityView.setRepeatability(repeatability);
    }

    @Nonnull
    @Override
    public Repeatability getRepeatability() {
        return repeatabilityView.getRepeatability();
    }

    @Override
    public void setLabelChangedHandler(@Nonnull Consumer<LanguageMap> runnable) {
        this.labelChangedHander = checkNotNull(runnable);
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        this.readOnlyCheckBox.setValue(readOnly);
    }

    @Override
    public boolean isReadOnly() {
        return readOnlyCheckBox.getValue();
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getFieldDescriptorViewContainer() {
        return fieldViewContainer;
    }

    @Nonnull
    @Override
    public ExpansionState getInitialExpansionState() {
        if(initialExpansionStateExpanded.getValue()) {
            return ExpansionState.EXPANDED;
        }
        else {
            return ExpansionState.COLLAPSED;
        }
    }

    @Override
    public void setInitialExpansionState(@Nonnull ExpansionState expansionState) {
        if(expansionState.equals(ExpansionState.EXPANDED)) {
            initialExpansionStateExpanded.setValue(true);
        }
        else {
            initialExpansionStateCollapsed.setValue(true);
        }
    }
}
