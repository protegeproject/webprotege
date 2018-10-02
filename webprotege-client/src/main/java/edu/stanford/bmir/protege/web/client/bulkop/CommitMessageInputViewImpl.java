package edu.stanford.bmir.protege.web.client.bulkop;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextArea;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 1 Oct 2018
 */
public class CommitMessageInputViewImpl extends Composite implements CommitMessageInputView {

    interface CommitMessageInputViewImplUiBinder extends UiBinder<HTMLPanel, CommitMessageInputViewImpl> {

    }

    private static CommitMessageInputViewImplUiBinder ourUiBinder = GWT.create(CommitMessageInputViewImplUiBinder.class);

    @UiField
    TextArea textArea;

    @Inject
    public CommitMessageInputViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        textArea.setFocus(true);
        textArea.setCursorPos(textArea.getText().length());
    }

    @Override
    public void setDefaultCommitMessage(@Nonnull String message) {
        textArea.setText(checkNotNull(message));
    }

    @Nonnull
    @Override
    public String getCommitMessage() {
        return textArea.getValue().trim();
    }

    @UiHandler("textArea")
    public void textAreaKeyPress(KeyDownEvent event) {
        if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER && event.isControlKeyDown()) {
            event.preventDefault();
        }
    }

    @UiHandler("textArea")
    public void textAreaKeyUp(KeyUpEvent event) {
        if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER && event.isControlKeyDown()) {
            event.preventDefault();
        }
    }
}