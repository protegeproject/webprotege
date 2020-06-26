package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-17
 */
public class FormIdViewImpl extends Composite implements FormIdView {

    interface FormIdViewImplUiBinder extends UiBinder<HTMLPanel, FormIdViewImpl> {

    }

    private static FormIdViewImplUiBinder ourUiBinder = GWT.create(FormIdViewImplUiBinder.class);

    @UiField
    Label displayNameField;

    @Inject
    public FormIdViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setFormDisplayName(@Nonnull String displayName) {
        displayNameField.setText(checkNotNull(displayName));
    }
}
