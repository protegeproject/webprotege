package edu.stanford.bmir.protege.web.client.form.input;

import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.HasReadOnly;

import javax.annotation.Nonnull;
import javax.inject.Inject;

public class CheckBox implements IsWidget, HasValue<Boolean>, HasText, HasEnabled, HasReadOnly {

    @Nonnull
    private final SimplePanel container = new SimplePanel();

    @Nonnull
    private final CheckBoxPresenter presenter;

    @Inject
    public CheckBox() {
        presenter = new CheckBoxPresenter(new CheckBoxViewImpl());
        presenter.start(container);
    }

    @Override
    public Widget asWidget() {
        return container;
    }

    @Override
    public Boolean getValue() {
        return presenter.getValue();
    }

    @Override
    public void setValue(Boolean value) {
        presenter.setValue(value);
    }

    @Override
    public void setValue(Boolean value, boolean fireEvents) {
        presenter.setValue(value, fireEvents);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Boolean> handler) {
        return presenter.addValueChangeHandler(handler);
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        presenter.fireEvent(event);
    }

    @Override
    public String getText() {
        return presenter.getText();
    }

    @Override
    public void setText(String text) {
        presenter.setText(text);
    }

    @Override
    public boolean isEnabled() {
        return presenter.isEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        presenter.setEnabled(enabled);
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        presenter.setReadOnly(readOnly);
    }

    @Override
    public boolean isReadOnly() {
        return presenter.isReadOnly();
    }

    public void setFocus(boolean focus) {
        presenter.setFocus(focus);
    }
}
