package edu.stanford.bmir.protege.web.client.app;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasText;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14/03/16
 */
public class ForbiddenViewImpl extends Composite implements ForbiddenView {

    interface ForbiddenViewImplUiBinder extends UiBinder<HTMLPanel, ForbiddenViewImpl> {

    }

    private static ForbiddenViewImplUiBinder ourUiBinder = GWT.create(ForbiddenViewImplUiBinder.class);

    @UiField
    protected HasText subMessage;

    @Inject
    public ForbiddenViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setSubMessage(@Nonnull String message) {
        SafeHtmlBuilder builder = new SafeHtmlBuilder();
        builder.appendEscaped(message);
        String safeHtml = builder.toSafeHtml().asString();
        subMessage.setText(safeHtml);
    }

    @Override
    public void clearSubMessage() {
        subMessage.setText("");
    }
}