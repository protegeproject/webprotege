package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.editor.ValueListEditor;
import edu.stanford.bmir.protege.web.client.editor.ValueListFlexEditorImpl;
import edu.stanford.bmir.protege.web.client.ui.Counter;
import edu.stanford.bmir.protege.web.shared.form.field.ChoiceDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.SingleChoiceControlType;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-18
 */
public class SingleChoiceControlDescriptorViewImpl extends Composite implements SingleChoiceControlDescriptorView {

    interface ChoiceControlDescriptorViewImplUiBinder extends UiBinder<HTMLPanel, SingleChoiceControlDescriptorViewImpl> {

    }

    private static ChoiceControlDescriptorViewImplUiBinder ourUiBinder = GWT.create(
            ChoiceControlDescriptorViewImplUiBinder.class);



    @UiField
    RadioButton segmentedRadio;

    @UiField
    RadioButton radioRadio;

    @UiField
    RadioButton comboBoxRadio;

    @UiField
    SimplePanel sourceContainer;

    @UiField(provided = true)
    static Counter counter = new Counter();

    @Inject
    public SingleChoiceControlDescriptorViewImpl(Provider<ChoiceDescriptorPresenter> choiceDescriptorPresenterProvider) {
        initWidget(ourUiBinder.createAndBindUi(this));
        counter.increment();
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getSourceContainer() {
        return sourceContainer;
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
}
