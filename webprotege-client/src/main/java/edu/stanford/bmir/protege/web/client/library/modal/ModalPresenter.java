package edu.stanford.bmir.protege.web.client.library.modal;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27 Sep 2018
 */
public class ModalPresenter {

    private final ModalView view;

    private final Map<DialogButton, ModalButtonHandler> handlerMap = new HashMap<>();

    private final ModalCloser modalCloser = this::hide;

    @Nullable
    private DialogButton primaryButton = null;

    @Inject
    public ModalPresenter(ModalView view) {
        this.view = checkNotNull(view);
        this.view.setCloser(modalCloser);
        this.view.setAcceptKeyHandler(this::handleAcceptKey);
        this.view.setEscapeKeyHandler(this::handleEscapeKey);
    }

    public void setTitle(@Nonnull String title) {
        this.view.setModalTitle(title);
    }

    public void addEscapeButton(DialogButton button) {
        view.addEscapeButton(button, createHandler(button));
        setButtonHandler(button, ModalCloser::closeModal);
    }

    public void addButton(@Nonnull DialogButton button) {
        view.addButton(button, createHandler(button));
    }

    private ModalButtonHandler createHandler(@Nonnull DialogButton button) {
        return closer -> Optional.ofNullable(handlerMap.get(button))
                .ifPresent(h -> h.handleModalButton(closer));
    }

    public void setPrimaryButton(@Nonnull DialogButton button) {
        this.primaryButton = button;
        view.addPrimaryButton(button, createHandler(button));
    }

    public void setButtonHandler(@Nonnull DialogButton button, @Nonnull ModalButtonHandler handler) {
        handlerMap.put(button, handler);
    }

    private void handleAcceptKey() {
        if(primaryButton != null) {
            ModalButtonHandler modalButtonHandler = handlerMap.get(primaryButton);
            if(modalButtonHandler != null) {
                modalButtonHandler.handleModalButton(modalCloser);
            }
        }
    }

    private void handleEscapeKey() {
        hide();
    }

    public void show(@Nonnull ModalCallback callback) {
        RootPanel rootPanel = RootPanel.get();
        rootPanel.add(view);
        callback.start(getContentContainer());
    }

    /**
     * Gets the content container, wrapping it so that it's possible
     * to request focus after it is shown
     */
    private AcceptsOneWidget getContentContainer() {
        return content -> {
            view.getModalContainer().setWidget(content);
            if(view instanceof HasRequestFocus) {
                ((HasRequestFocus) view).requestFocus();
            }
        };
    }

    public void hide() {
        view.hide();
    }
}
