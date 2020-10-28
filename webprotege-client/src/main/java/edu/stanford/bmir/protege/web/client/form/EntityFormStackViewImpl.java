package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.FormsMessages;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageStyle;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-23
 */
public class EntityFormStackViewImpl extends Composite implements EntityFormStackView {

    interface EntityFormStackViewImplUiBinder extends UiBinder<HTMLPanel, EntityFormStackViewImpl> {

    }

    private static EntityFormStackViewImplUiBinder ourUiBinder = GWT.create(EntityFormStackViewImplUiBinder.class);

    @Nonnull
    private final MessageBox messageBox;

    @Nonnull
    private FormsMessages formsMessages;

    @Nonnull
    private EnterEditModeHandler enterEditModeHandler = () -> {};

    @Nonnull
    private ApplyEditsHandler applyEditsHandler = () -> {};

    @Nonnull
    private CancelEditsHandler cancelEditsHandler = () -> {};

    @Nonnull
    private DeprecateEntityHandler deprecateEntityHandler = () -> {};

    @UiField
    AcceptsOneWidget formsStackContainer;

    @UiField
    AcceptsOneWidget langTagFilterContainer;

    @UiField
    Button applyEditsButton;

    @UiField
    Button cancelEditsButton;

    @UiField
    Button editButton;

    @UiField
    Button deprecateButton;

    @Inject
    public EntityFormStackViewImpl(MessageBox messageBox, @Nonnull FormsMessages formsMessages) {
        this.messageBox = checkNotNull(messageBox);
        this.formsMessages = checkNotNull(formsMessages);
        initWidget(ourUiBinder.createAndBindUi(this));
        editButton.addClickHandler(event -> enterEditModeHandler.handleEnterEditMode());
        applyEditsButton.addClickHandler(event -> applyEditsHandler.handleApplyEdits());
        cancelEditsButton.addClickHandler(event -> cancelEditsHandler.handleCancelEdits());
        deprecateButton.addClickHandler(event -> deprecateEntityHandler.handleDeprecateEntity());
    }

    @Override
    public void displayApplyOutstandingEditsConfirmation(@Nonnull ApplyEditsHandler applyEditsHandler,
                                                         @Nonnull CancelEditsHandler cancelEditsHandler) {
        messageBox.showConfirmBox(MessageStyle.ALERT,
                                  formsMessages.saveEditsAlert_Title(),
                                  formsMessages.saveEditsAlert_Message(),
                                  DialogButton.NO,
                                  cancelEditsHandler::handleCancelEdits,
                                  DialogButton.YES,
                                  applyEditsHandler::handleApplyEdits,
                                  DialogButton.YES);
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getFormStackContainer() {
        return formsStackContainer;
    }    @Nonnull
    @Override
    public AcceptsOneWidget getLangTagFilterContainer() {
        return langTagFilterContainer;
    }


    @Override
    public void setEnterEditModeHandler(@Nonnull EnterEditModeHandler enterEditModeHandler) {
        this.enterEditModeHandler = checkNotNull(enterEditModeHandler);
    }

    @Override
    public void setApplyEditsHandler(@Nonnull ApplyEditsHandler handler) {
        this.applyEditsHandler = checkNotNull(handler);
    }

    @Override
    public void setCancelEditsHandler(@Nonnull CancelEditsHandler handler) {
        this.cancelEditsHandler = checkNotNull(handler);
    }

    @Override
    public void setEditButtonVisible(boolean visible) {
        editButton.setVisible(visible);
    }

    @Override
    public void setApplyEditsButtonVisible(boolean visible) {
        applyEditsButton.setVisible(visible);
    }

    @Override
    public void setCancelEditsButtonVisible(boolean visible) {
        cancelEditsButton.setVisible(visible);
    }

    @Override
    public void setDeprecateButtonVisible(boolean visible) {
        this.deprecateButton.setVisible(visible);
    }

    @Override
    public void setDeprecateEntityHandler(DeprecateEntityHandler handler) {
        this.deprecateEntityHandler = checkNotNull(handler);
    }
}
