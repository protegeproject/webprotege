package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.form.data.FormControlData;
import edu.stanford.bmir.protege.web.shared.form.data.PrimitiveFormControlData;
import edu.stanford.bmir.protege.web.shared.form.data.SingleChoiceControlData;
import edu.stanford.bmir.protege.web.shared.form.field.ChoiceDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.SingleChoiceControlDescriptor;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Jul 2017
 */
public class SegmentedButtonChoiceControl extends Composite implements SingleChoiceControl {

    private static final int SEGMENT_SIZE = 120;

    private final ChoiceDescriptorSupplier choiceDescriptorSupplier;

    private SingleChoiceControlDescriptor descriptor;

    private Optional<PrimitiveFormControlData> mostRecentSetValue = Optional.empty();

    private boolean enabled = true;

    interface SegmentedButtonChoiceControlUiBinder extends UiBinder<HTMLPanel, SegmentedButtonChoiceControl> {

    }

    private static SegmentedButtonChoiceControlUiBinder ourUiBinder = GWT.create(SegmentedButtonChoiceControlUiBinder.class);

    @UiField
    FocusPanel focusPanel;

    @UiField
    HTMLPanel segmentContainer;

    private int selectedIndex = -1;

    private boolean dirty = false;

    private final List<PrimitiveFormControlData> choices = new ArrayList<>();

    private final List<InlineLabel> choiceWidgets = new ArrayList<>();

    private Optional<PrimitiveFormControlData> defaultChoice = Optional.empty();

    @Inject
    public SegmentedButtonChoiceControl(ChoiceDescriptorSupplier choiceDescriptorSupplier) {
        this.choiceDescriptorSupplier = checkNotNull(choiceDescriptorSupplier);
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public boolean isWellFormed() {
        return selectedIndex != -1;
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return addHandler(handler, DirtyChangedEvent.TYPE);
    }

    @Override
    public boolean isDirty() {
        return dirty;
    }

    @Override
    public void setDescriptor(@Nonnull SingleChoiceControlDescriptor descriptor) {
        this.descriptor = checkNotNull(descriptor);
        choiceDescriptorSupplier.getChoices(this.descriptor.getSource(), this::setChoices);
        descriptor.getDefaultChoice().ifPresent(this::setDefaultChoice);
    }

    private void setChoices(List<ChoiceDescriptor> choices) {
        dirty = false;
        segmentContainer.clear();
        choiceWidgets.clear();
        this.choices.clear();
        this.selectedIndex = -1;
        String langTag = LocaleInfo.getCurrentLocale().getLocaleName();
        for(ChoiceDescriptor choice : choices) {
            InlineLabel label = new InlineLabel(choice.getLabel().get(langTag));
            segmentContainer.add(label);
            this.choices.add(choice.getValue());
            this.choiceWidgets.add(label);
            label.addClickHandler(event -> {
                if(enabled) {
                    setSelection(choice.getValue(), true);
                }
            });
        }
        mostRecentSetValue.ifPresent(setValue -> setSelection(setValue, true));
        segmentContainer.getElement().getStyle().setProperty("maxWidth", SEGMENT_SIZE * choices.size(), Style.Unit.PX);
        setDefaultChoiceSelected();
    }

    private void setDefaultChoiceSelected() {
        defaultChoice.ifPresent(v -> setSelection(v, false));
    }

    private void setSelection(PrimitiveFormControlData dataValue, boolean fireEvents) {
        int nextIndex = choices.indexOf(dataValue);
        if(nextIndex != selectedIndex) {
            selectedIndex = nextIndex;
            updateStylesBasedOnSelection();
            if (fireEvents) {
                dirty = true;
                fireEvent(new DirtyChangedEvent());
                ValueChangeEvent.fire(this, getValue());
            }
        }
    }

    private void updateStylesBasedOnSelection() {
        int currentIndex = 0;
        for(Widget widget : choiceWidgets) {
            if(currentIndex == selectedIndex) {
                widget.addStyleName(WebProtegeClientBundle.BUNDLE.style().selection());
            }
            else {
                widget.removeStyleName(WebProtegeClientBundle.BUNDLE.style().selection());
            }
            currentIndex++;
        }
    }

    @Override
    public void setValue(FormControlData object) {
        mostRecentSetValue = Optional.empty();
        if(object instanceof SingleChoiceControlData) {
            Optional<PrimitiveFormControlData> choice = ((SingleChoiceControlData) object).getChoice();
            mostRecentSetValue = choice;
            if(choice.isPresent()) {
                setSelection(choice.get(), false);
            }
            else {
                clearValue();
            }
        }
        else {
            clearValue();
        }
    }

    public void setDefaultChoice(@Nonnull ChoiceDescriptor choice) {
        defaultChoice = Optional.of(choice.getValue());
    }

    @Override
    public void clearValue() {
        dirty = false;
        selectedIndex = -1;
        updateStylesBasedOnSelection();
        setDefaultChoiceSelected();
    }

    @Override
    public Optional<FormControlData> getValue() {
        if(selectedIndex == -1) {
            return Optional.empty();
        }
        PrimitiveFormControlData dataValue = choices.get(selectedIndex);
        return Optional.of(SingleChoiceControlData.get(descriptor, dataValue));
    }

    private void incrementSelection() {
        if(selectedIndex + 1 < choices.size()) {
            PrimitiveFormControlData choice = choices.get(selectedIndex + 1);
            setSelection(choice, true);
        }
    }

    private void decrementSelection() {
        if(selectedIndex - 1 > -1) {
            PrimitiveFormControlData choice = choices.get(selectedIndex - 1);
            setSelection(choice, true);
        }
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<FormControlData>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @UiHandler("focusPanel")
    public void focusPanelKeyUp(KeyUpEvent event) {
        if(enabled) {
            if(event.getNativeKeyCode() == KeyCodes.KEY_LEFT) {
                decrementSelection();
            }
            else if(event.getNativeKeyCode() == KeyCodes.KEY_RIGHT) {
                incrementSelection();
            }
        }
    }

    @Override
    public void requestFocus() {
        focusPanel.setFocus(true);
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
