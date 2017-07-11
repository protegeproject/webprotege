package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataValue;
import edu.stanford.bmir.protege.web.shared.form.field.ChoiceDescriptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Jul 2017
 */
public class ChoiceFieldSegmentedEditor extends Composite implements ChoiceFieldEditor {

    private static final int SEGMENT_SIZE = 120;

    interface ChoiceFieldSegmentedEditorUiBinder extends UiBinder<HTMLPanel, ChoiceFieldSegmentedEditor> {

    }

    private static ChoiceFieldSegmentedEditorUiBinder ourUiBinder = GWT.create(ChoiceFieldSegmentedEditorUiBinder.class);

    @UiField
    FocusPanel focusPanel;

    @UiField
    HTMLPanel segmentContainer;

    private int selectedIndex = -1;

    private boolean dirty = false;

    private final List<FormDataValue> choices = new ArrayList<>();

    private final List<InlineLabel> choiceWidgets = new ArrayList<>();

    private Optional<FormDataValue> defaultChoice = Optional.empty();

    public ChoiceFieldSegmentedEditor() {
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
    public void setChoices(List<ChoiceDescriptor> choices) {
        dirty = false;
        segmentContainer.clear();
        choiceWidgets.clear();
        this.choices.clear();
        this.selectedIndex = -1;
        for(ChoiceDescriptor choice : choices) {
            InlineLabel label = new InlineLabel(choice.getLabel());
            segmentContainer.add(label);
            this.choices.add(choice.getValue());
            this.choiceWidgets.add(label);
            label.addClickHandler(event -> {
                setSelection(choice.getValue(), true);
            });
        }
        segmentContainer.getElement().getStyle().setWidth(SEGMENT_SIZE * choices.size(), Style.Unit.PX);
        setDefaultChoiceSelected();
    }

    private void setDefaultChoiceSelected() {
        defaultChoice.ifPresent(v -> setSelection(v, false));
    }

    private void setSelection(FormDataValue dataValue, boolean fireEvents) {
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
    public void setValue(FormDataValue object) {
        setSelection(object, false);
    }

    @Override
    public void setDefaultChoices(List<FormDataValue> defaultChoices) {
        if(defaultChoices.isEmpty()) {
            defaultChoice = Optional.empty();
        }
        else {
            defaultChoice = Optional.of(defaultChoices.get(0));
        }
    }

    @Override
    public void clearValue() {
        dirty = false;
        selectedIndex = -1;
        updateStylesBasedOnSelection();
        setDefaultChoiceSelected();
    }

    @Override
    public Optional<FormDataValue> getValue() {
        if(selectedIndex == -1) {
            return Optional.empty();
        }
        FormDataValue dataValue = choices.get(selectedIndex);
        return Optional.of(dataValue);
    }

    private void incrementSelection() {
        if(selectedIndex + 1 < choices.size()) {
            FormDataValue choice = choices.get(selectedIndex + 1);
            setSelection(choice, true);
        }
    }

    private void decrementSelection() {
        if(selectedIndex - 1 > -1) {
            FormDataValue choice = choices.get(selectedIndex - 1);
            setSelection(choice, true);
        }
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<FormDataValue>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @UiHandler("focusPanel")
    public void focusPanelKeyUp(KeyUpEvent event) {
        if(event.getNativeKeyCode() == KeyCodes.KEY_LEFT) {
            decrementSelection();
        }
        else if(event.getNativeKeyCode() == KeyCodes.KEY_RIGHT) {
            incrementSelection();
        }
    }
}