package edu.stanford.bmir.protege.web.client.progress;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasVisibility;
import com.google.gwt.user.client.ui.Label;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 1 Mar 2017
 */
public class BusyViewImpl extends Composite implements BusyView, HasVisibility {

    interface BusyViewImplUiBinder extends UiBinder<HTMLPanel, BusyViewImpl> {

    }

    @UiField
    protected Label messageLabel;

    private static BusyViewImplUiBinder ourUiBinder = GWT.create(BusyViewImplUiBinder.class);

    @Inject
    public BusyViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    public void setMessage(@Nonnull String message) {
        messageLabel.setText(checkNotNull(message));
        messageLabel.setVisible(true);
    }

    public void clearMessage() {
        messageLabel.setText("");
        messageLabel.setVisible(false);
    }
}