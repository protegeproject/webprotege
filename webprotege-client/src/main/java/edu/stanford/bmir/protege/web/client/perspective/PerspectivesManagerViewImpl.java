package edu.stanford.bmir.protege.web.client.perspective;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

public class PerspectivesManagerViewImpl extends Composite implements PerspectivesManagerView {

    private ResetPerspectivesHandler resetPerspectivesHandler;

    interface PerspectivesManagerViewImplUiBinder extends UiBinder<HTMLPanel, PerspectivesManagerViewImpl> {
    }

    private static PerspectivesManagerViewImplUiBinder ourUiBinder = GWT.create(PerspectivesManagerViewImplUiBinder.class);

    @UiField
    SimplePanel container;

    @UiField
    Button resetPerspectivesButton;

    @Nonnull
    private MessageBox messageBox;

    @Nonnull
    private Messages messages;

    @Inject
    public PerspectivesManagerViewImpl(@Nonnull MessageBox messageBox,
                                       @Nonnull Messages messages) {
        this.messageBox = checkNotNull(messageBox);
        this.messages = messages;
        initWidget(ourUiBinder.createAndBindUi(this));
        resetPerspectivesButton.addClickHandler(this::handleResetPerspectives);
    }

    private void handleResetPerspectives(ClickEvent event) {
        resetPerspectivesHandler.handleResetPerspectives();
    }

    @Override
    public void setResetPerspectivesHandler(@Nonnull ResetPerspectivesHandler handler) {
        resetPerspectivesHandler = checkNotNull(handler);
    }

    @Override
    public void displayResetPerspectivesConfirmation(Runnable resetHandler) {
        messageBox.showConfirmBox(messages.perspective_resetConfirmation_title(),
                                  messages.perspective_resetConfirmation_message(),
                                  DialogButton.NO,
                                  DialogButton.YES,
                                  resetHandler,
                                  DialogButton.NO);
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getPerspectivesListContainer() {
        return container;
    }
}