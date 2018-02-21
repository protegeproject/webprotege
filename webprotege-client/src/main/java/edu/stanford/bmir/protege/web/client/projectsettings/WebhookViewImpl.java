package edu.stanford.bmir.protege.web.client.projectsettings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;

import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 8 Jun 2017
 */
public class WebhookViewImpl extends Composite implements WebhookView {

    interface WebhookViewImplUiBinder extends UiBinder<HTMLPanel, WebhookViewImpl> {

    }

    private static WebhookViewImplUiBinder ourUiBinder = GWT.create(WebhookViewImplUiBinder.class);

    @UiField
    TextBox payloadUrlField;

    private boolean dirty = false;

    public WebhookViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }


    @UiHandler("payloadUrlField")
    protected void handlePayloadChanged(ValueChangeEvent<String> event) {
        dirty = true;
        ValueChangeEvent.fire(this, getValue());
    }

    @Override
    public boolean isWellFormed() {
        return !payloadUrlField.getText().trim().isEmpty();
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return payloadUrlField.addHandler(handler, DirtyChangedEvent.TYPE);
    }

    @Override
    public void setValue(String object) {
        dirty = false;
        payloadUrlField.setText(object);
    }

    @Override
    public void clearValue() {
        dirty = false;
        payloadUrlField.setText("");
    }

    @Override
    public Optional<String> getValue() {
        String payloadUrl = payloadUrlField.getText().trim();
        if(payloadUrl.isEmpty()) {
            return Optional.empty();
        }
        else {
            return Optional.of(payloadUrl);
        }
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<String>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public boolean isDirty() {
        return dirty;
    }
}