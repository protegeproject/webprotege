package edu.stanford.bmir.protege.web.client.library.modal;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27 Sep 2018
 */
public class ModalPresenter {

    @Nonnull
    private final ModalView view;

    @Nonnull
    private final Map<DialogButton, ModalButtonHandler> handlerMap = new HashMap<>();

    @Nonnull
    private ModalCloser modalCloser = () -> {};

    @Nullable
    private DialogButton primaryButton = null;

    @Inject
    public ModalPresenter(ModalView view) {
        this.view = checkNotNull(view);
    }

    @Nonnull
    public ModalView getView() {
        return view;
    }

    public void setTitle(@Nonnull String title) {
        this.view.setCaption(title);
    }

    public void setView(@Nonnull IsWidget content) {
        view.setContent(checkNotNull(content));
    }

    public void setEscapeButton(@Nonnull DialogButton button) {
        view.setEscapeButton(button, () -> runHandler(button));
        setButtonHandler(button, ModalCloser::closeModal);
    }

    public void setPrimaryButton(@Nonnull DialogButton button) {
        this.primaryButton = button;
        view.setPrimaryButton(button, () -> runHandler(button));
    }

    public void addButton(@Nonnull DialogButton button) {
        view.addButton(button, () -> runHandler(button));
    }

    protected void setModalCloser(@Nonnull ModalCloser modalCloser) {
        this.modalCloser = checkNotNull(modalCloser);
    }

    private void runHandler(@Nonnull DialogButton button) {
        handlerMap.getOrDefault(button, (closer) -> {}).handleModalButton(modalCloser);
    }

    public void setButtonHandler(@Nonnull DialogButton button, @Nonnull ModalButtonHandler handler) {
        handlerMap.put(checkNotNull(button), checkNotNull(handler));
    }

    private void handleEscapeKey() {
        modalCloser.closeModal();
    }

    protected void show() {
        RootPanel rootPanel = RootPanel.get();
        rootPanel.add(view);
    }

    protected void hide() {
        view.hide();
    }

    public void setPrimaryButtonFocusedOnShow(boolean primaryButtonFocusedOnShow) {
        view.setPrimaryButtonFocusedOnAttach(primaryButtonFocusedOnShow);
    }

    public void accept() {
        if (primaryButton != null) {
            ModalButtonHandler modalButtonHandler = handlerMap.get(primaryButton);
            if (modalButtonHandler != null) {
                modalButtonHandler.handleModalButton(modalCloser);
            }
        }
    }

    protected void escape() {
        modalCloser.closeModal();
    }
}
