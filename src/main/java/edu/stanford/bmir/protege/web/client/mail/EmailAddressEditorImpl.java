package edu.stanford.bmir.protege.web.client.mail;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.user.EmailAddress;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/11/2013
 */
public class EmailAddressEditorImpl extends Composite implements EmailAddressEditor {

    interface EmailAddressEditorImplUiBinder extends UiBinder<HTMLPanel, EmailAddressEditorImpl> {

    }

    private static EmailAddressEditorImplUiBinder ourUiBinder = GWT.create(EmailAddressEditorImplUiBinder.class);

    @UiField
    protected TextBox emailAddressField;

    @UiField
    protected TextBox confirmEmailAddressField;

    private boolean dirty = false;

    @UiHandler("emailAddressField")
    protected void handleEmailAddressChanged(ValueChangeEvent<String> evt) {

    }

    @UiHandler("confirmEmailAddressField")
    protected void handleConfirmEmailAddressChanged(ValueChangeEvent<String> evt) {

    }

    public EmailAddressEditorImpl() {
        WebProtegeClientBundle.BUNDLE.style().ensureInjected();
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
    }

    @Override
    public Optional<Focusable> getInitialFocusable() {
        return Optional.of(emailAddressField);
    }

    @Override
    public void setValue(EmailAddress object) {
        setEmailAddressValue(object.getEmailAddress());
    }

    @Override
    public void clearValue() {
        setEmailAddressValue("");
    }

    private void setEmailAddressValue(String value) {
        emailAddressField.setText(value);
        confirmEmailAddressField.setValue(value);
        dirty = false;
    }

    @Override
    public Optional<EmailAddress> getValue() {
        if(isWellFormed()) {
            return Optional.of(new EmailAddress(emailAddressField.getText().trim()));
        }
        else {
            return Optional.absent();
        }
    }

    @Override
    public boolean isDirty() {
        return dirty;
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return addHandler(handler, DirtyChangedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<EmailAddress>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public boolean isWellFormed() {
        return emailAddressField.getText().trim().equals(confirmEmailAddressField.getText().trim());
    }
}
