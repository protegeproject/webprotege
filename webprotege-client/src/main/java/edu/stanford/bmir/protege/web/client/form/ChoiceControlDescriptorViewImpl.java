package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RadioButton;
import edu.stanford.bmir.protege.web.client.editor.ValueListEditor;
import edu.stanford.bmir.protege.web.client.editor.ValueListFlexEditorImpl;
import edu.stanford.bmir.protege.web.client.ui.Counter;
import edu.stanford.bmir.protege.web.shared.form.field.ChoiceDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.SingleChoiceControlType;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Collections;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-18
 */
public class ChoiceControlDescriptorViewImpl extends Composite implements ChoiceControlDescriptorView {

    interface ChoiceControlDescriptorViewImplUiBinder extends UiBinder<HTMLPanel, ChoiceControlDescriptorViewImpl> {

    }

    private static ChoiceControlDescriptorViewImplUiBinder ourUiBinder = GWT.create(
            ChoiceControlDescriptorViewImplUiBinder.class);

    @UiField(provided = true)
    ValueListEditor<ChoiceDescriptor> choiceListEditor;

    @UiField
    RadioButton segmentedRadio;

    @UiField
    RadioButton radioRadio;

    @UiField
    RadioButton comboBoxRadio;

    @UiField(provided = true)
    static Counter counter = new Counter();

    @Inject
    public ChoiceControlDescriptorViewImpl(Provider<ChoiceDescriptorPresenter> choiceDescriptorPresenterProvider) {
        choiceListEditor = new ValueListFlexEditorImpl<>(choiceDescriptorPresenterProvider::get);
        choiceListEditor.setEnabled(true);
        choiceListEditor.setNewRowMode(ValueListEditor.NewRowMode.MANUAL);
        counter.increment();
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Nonnull
    @Override
    public SingleChoiceControlType getWidgetType() {
        if(comboBoxRadio.getValue()) {
            return SingleChoiceControlType.COMBO_BOX;
        }
        else if(radioRadio.getValue()) {
            return SingleChoiceControlType.RADIO_BUTTON;
        }
        else {
            return SingleChoiceControlType.SEGMENTED_BUTTON;
        }
    }

    @Override
    public void setWidgetType(@Nonnull SingleChoiceControlType widgetType) {
        if(widgetType == SingleChoiceControlType.RADIO_BUTTON) {
            radioRadio.setValue(true);
        }
        else if(widgetType == SingleChoiceControlType.COMBO_BOX) {
            comboBoxRadio.setValue(true);
        }
        else {
            segmentedRadio.setValue(true);
        }
    }

    @Override
    public void setChoiceDescriptors(@Nonnull List<ChoiceDescriptor> choiceDescriptors) {
        choiceListEditor.setValue(choiceDescriptors);
    }

    @Nonnull
    @Override
    public ImmutableList<ChoiceDescriptor> getChoiceDescriptors() {
        return choiceListEditor.getEditorValue().map(ImmutableList::copyOf).orElse(ImmutableList.of());
    }
}
