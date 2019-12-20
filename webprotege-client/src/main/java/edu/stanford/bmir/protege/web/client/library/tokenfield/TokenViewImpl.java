package edu.stanford.bmir.protege.web.client.library.tokenfield;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-14
 */
public class TokenViewImpl extends Composite implements TokenView {

    private RemoveTokenRequestHandler removeTokenRequestHandler = () -> {};

    interface TokenViewImplUiBinder extends UiBinder<HTMLPanel, TokenViewImpl> {

    }

    private static TokenViewImplUiBinder ourUiBinder = GWT.create(TokenViewImplUiBinder.class);

    @UiField
    Label labelField;

    @UiField
    Button removeButton;

    @Inject
    public TokenViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        removeButton.addClickHandler(event -> removeTokenRequestHandler.handleRemoveTokenRequest());
    }

    @Override
    public void setLabel(@Nonnull String label) {
        labelField.setText(label.trim());
    }

    @Override
    public void setRemoveTokenRequestHandler(@Nonnull RemoveTokenRequestHandler handler) {
        this.removeTokenRequestHandler = checkNotNull(handler);
    }
}
