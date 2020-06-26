package edu.stanford.bmir.protege.web.client.form.input;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.HasReadOnly;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

public class CheckBoxPresenter implements HasValue<Boolean>, HasText, HasEnabled, HasReadOnly {

    @Nonnull
    private final CheckBoxView view;

    @Inject
    public CheckBoxPresenter(@Nonnull CheckBoxView view) {
        this.view = checkNotNull(view);
        view.addKeyUpHandler(this::handleKeyUp);
        view.addMouseDownHandler(this::handleMouseDown);
        view.addMouseUpHandler(this::handleMouseUp);
    }


    private void handleMouseUp(MouseUpEvent mouseUpEvent) {
        toggleSelected();
    }

    private void handleMouseDown(MouseDownEvent mouseDownEvent) {

    }

    private void handleKeyUp(KeyUpEvent keyUpEvent) {
        if(keyUpEvent.getNativeKeyCode() == KeyCodes.KEY_SPACE) {
            keyUpEvent.stopPropagation();
            keyUpEvent.preventDefault();
            toggleSelected();
        }
    }

    private void toggleSelected() {
        if(view.isReadOnly() || !view.isEnabled()) {
            return;
        }
        boolean sel = view.getValue();
        boolean nextSel = !sel;
        view.setValue(nextSel);
    }

    @Override
    public Boolean getValue() {
        return view.getValue();
    }


    public void start(AcceptsOneWidget container) {
        container.setWidget(view);
    }

    @Override
    public void setValue(Boolean value) {
        view.setValue(value);
    }

    @Override
    public void setValue(Boolean value, boolean fireEvents) {
        view.setValue(value, fireEvents);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Boolean> handler) {
        return view.addValueChangeHandler(handler);
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        view.fireEvent(event);
    }

    @Override
    public String getText() {
        return view.getText();
    }

    @Override
    public void setText(String text) {
        view.setText(text);
    }

    @Override
    public boolean isEnabled() {
        return view.isEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        view.setEnabled(enabled);
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        view.setReadOnly(readOnly);
    }

    @Override
    public boolean isReadOnly() {
        return view.isReadOnly();
    }

    public void setFocus(boolean focus) {
        view.setFocus(focus);
    }
}
