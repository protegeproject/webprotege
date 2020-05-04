package edu.stanford.bmir.protege.web.client.library.tokenfield;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-14
 */
public class TokenFieldViewImpl extends Composite implements TokenFieldView {

    interface TokenFieldViewImplUiBinder extends UiBinder<HTMLPanel, TokenFieldViewImpl> {

    }

    private static TokenFieldViewImplUiBinder ourUiBinder = GWT.create(TokenFieldViewImplUiBinder.class);

    @UiField
    HTMLPanel container;

    @UiField
    Button addTokenButton;

    @UiField
    Label placeholder;

    private AddTokenHandler addTokenHandler = (event) -> {};

    @Inject
    public TokenFieldViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        addTokenButton.addClickHandler(event -> addTokenHandler.handleAddToken(event));
        placeholder.addClickHandler(event -> addTokenHandler.handleAddToken(event));
    }

    @Override
    public void clear() {
        container.clear();
        container.add(addTokenButton);
    }

    @Override
    public void setPlaceholder(String placeholder) {
        this.placeholder.setText(placeholder);
    }

    @Override
    public void setPlaceholderVisible(boolean visible) {
        placeholder.setVisible(visible);
    }

    @Override
    public void setAddTokenHandler(@Nonnull AddTokenHandler handler) {
        this.addTokenHandler = checkNotNull(handler);
    }

    @Override
    public void add(IsWidget view) {
        container.add(view);
        container.remove(addTokenButton);
        container.add(addTokenButton);
    }

    @Override
    public void remove(IsWidget view) {
        container.remove(view);
    }
}
