package edu.stanford.bmir.protege.web.client.chgpwd;

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
import edu.stanford.bmir.protege.web.shared.chgpwd.ResetPasswordData;

import javax.inject.Inject;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 01/10/2014
 */
public class ResetPasswordViewImpl extends Composite implements ResetPasswordView {

    interface ResetPasswordViewImplUiBinder extends UiBinder<HTMLPanel, ResetPasswordViewImpl> {
    }

    private static ResetPasswordViewImplUiBinder ourUiBinder = GWT.create(ResetPasswordViewImplUiBinder.class);

    @UiField
    protected TextBox emailAddressField;

    private boolean dirty = false;

    @Inject
    public ResetPasswordViewImpl() {
        WebProtegeClientBundle.BUNDLE.style().ensureInjected();
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
    }

    @UiHandler("emailAddressField")
    protected void handleEmailChanged(ValueChangeEvent<String> event) {
        dirty = true;
    }

    @Override
    public java.util.Optional<Focusable> getInitialFocusable() {
        return java.util.Optional.of(emailAddressField);
    }

    @Override
    public void setValue(ResetPasswordData object) {
        emailAddressField.setText(object.getEmailAddress());
        dirty = false;
    }

    @Override
    public void clearValue() {
        emailAddressField.setText("");
        dirty = false;
    }

    @Override
    public Optional<ResetPasswordData> getValue() {
        return Optional.of(new ResetPasswordData(emailAddressField.getValue()));
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
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<ResetPasswordData>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public boolean isWellFormed() {
        return getValue().isPresent();
    }
}
