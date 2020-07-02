package edu.stanford.bmir.protege.web.client.form.input;

import com.google.gwt.aria.client.CheckboxRole;
import com.google.gwt.aria.client.CheckedValue;
import com.google.gwt.aria.client.Roles;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

public class CheckBoxViewImpl extends Composite implements CheckBoxView {

    private boolean enabled = true;

    private boolean selected = false;

    private boolean readOnly = false;

    interface CheckBoxViewImplUiBinder extends UiBinder<HTMLPanel, CheckBoxViewImpl> {
    }

    private static CheckBoxViewImplUiBinder ourUiBinder = GWT.create(CheckBoxViewImplUiBinder.class);

    @UiField
    HTMLPanel input;

    @UiField
    Label label;

    @UiField
    CheckBoxResourceBundle cb;

    @Inject
    public CheckBoxViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        getElement().setPropertyString("role", "checkbox");
        sinkEvents(Event.ONKEYUP);
        sinkEvents(Event.ONMOUSEDOWN);
        sinkEvents(Event.ONMOUSEUP);
        cb.style().ensureInjected();
        updateState();
    }

    public void setFocusable(boolean focusable) {
        if (focusable) {
            getElement().setTabIndex(0);
        }
        else {
            getElement().removeAttribute("tabindex");
        }
    }

    @Override
    public HandlerRegistration addKeyUpHandler(KeyUpHandler handler) {
        return addHandler(handler, KeyUpEvent.getType());
    }

    @Override
    public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
        return addHandler(handler, MouseDownEvent.getType());
    }

    @Override
    public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
        return addHandler(handler, MouseUpEvent.getType());
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Boolean> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        updateState();
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        updateState();
    }

    @Override
    public Boolean getValue() {
        return selected;
    }

    @Override
    public void setValue(Boolean value) {
        setValue(value, true);
    }

    @Override
    public void setValue(Boolean value, boolean fireEvents) {
        if(this.selected != value) {
            this.selected = value;
            updateState();
            if (fireEvents) {
                ValueChangeEvent.fire(this, this.selected);
            }
        }
    }

    private void updateState() {
        CheckboxRole role = Roles.getCheckboxRole();
        Element element = getElement();
        if (selected) {
            role.setAriaCheckedState(element, CheckedValue.TRUE);
        }
        else {
            role.setAriaCheckedState(element, CheckedValue.FALSE);
        }
        role.setAriaDisabledState(element, !enabled);
        setFocusable(enabled);

    }

    @Override
    public String getText() {
        return label.getText();
    }

    @Override
    public void setText(String text) {
        label.setText(checkNotNull(text));
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        updateState();
    }

    @Override
    public boolean isReadOnly() {
        return readOnly;
    }

    @Override
    public void setFocus(boolean focus) {
        if (focus) {
            getElement().focus();
        }
        else {
            getElement().blur();
        }
    }
}