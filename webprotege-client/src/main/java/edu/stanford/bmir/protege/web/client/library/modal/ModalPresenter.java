package edu.stanford.bmir.protege.web.client.library.modal;

import com.google.gwt.user.client.ui.RootPanel;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;

import javax.annotation.Nonnull;
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

    @Inject
    public ModalPresenter(ModalView view) {
        this.view = checkNotNull(view);
        view.setCloser(this::hide);
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

    public void addPrimaryButton(@Nonnull DialogButton button) {
        view.addPrimaryButton(button, createHandler(button));
    }

    public void setButtonHandler(@Nonnull DialogButton button, @Nonnull ModalButtonHandler handler) {
        handlerMap.put(button, handler);
    }

    public void show(@Nonnull ModalCallback callback) {
        RootPanel rootPanel = RootPanel.get();
        rootPanel.add(view);
        callback.start(view.getModalContainer());
    }

    public void hide() {
        view.hide();
    }
}
