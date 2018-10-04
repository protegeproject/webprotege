package edu.stanford.bmir.protege.web.client.library.modal;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Stack;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 3 Oct 2018
 */
@ApplicationSingleton
public class ModalManager {

    @Nonnull
    private final Stack<ModalPresenter> modalStack = new Stack<>();

    @Nonnull
    private final Provider<ModalPresenter> modalPresenterProvider;

    private HandlerRegistration keyUpReg = null;

    @Inject
    public ModalManager(@Nonnull Provider<ModalPresenter> modalPresenterProvider) {
        this.modalPresenterProvider = checkNotNull(modalPresenterProvider);
        attachHandlers();
    }

    private void attachHandlers() {
        if(keyUpReg != null) {
            return;
        }
        RootPanel widgets = RootPanel.get();
        widgets.sinkEvents(Event.ONKEYUP);
        keyUpReg = widgets.addHandler(this::handleKeyUp, KeyUpEvent.getType());
    }

    private void handleKeyUp(@Nonnull KeyUpEvent event) {
        if(isAcceptAccelerator(event)) {
            handleAcceptAccelerator();
        }
        else if(isEscapeAccelerator(event)) {
            handleEscapeAccelerator();
        }
    }

    private void handleAcceptAccelerator() {
        if(modalStack.isEmpty()) {
            return;
        }
        ModalPresenter presenter = modalStack.pop();
        presenter.accept();
    }

    private void handleEscapeAccelerator() {
        if(modalStack.isEmpty()) {
            return;
        }
        ModalPresenter presenter = modalStack.pop();
        presenter.escape();
    }

    @Nonnull
    public ModalPresenter createPresenter() {
        return modalPresenterProvider.get();
    }

    public void showModal(@Nonnull ModalPresenter presenter) {
        if(modalStack.contains(presenter)) {
            throw new RuntimeException("Already showing modal for presenter");
        }
        modalStack.push(presenter);
        RootPanel rootPanel = RootPanel.get();
        rootPanel.add(presenter.getView());
        presenter.setModalCloser(() -> {
            modalStack.pop();
            presenter.hide();
        });
    }

    private void handleEscape() {
        ModalPresenter presenter = modalStack.pop();
        presenter.hide();
    }

    private void handleAccept() {
        if(modalStack.isEmpty()) {
            return;
        }
        ModalPresenter presenter = modalStack.pop();
        presenter.accept();
        presenter.hide();
    }


    private static boolean isAcceptAccelerator(@Nonnull KeyUpEvent event) {
        return event.getNativeKeyCode() == KeyCodes.KEY_ENTER && event.isControlKeyDown();
    }


    private static boolean isEscapeAccelerator(@Nonnull KeyUpEvent event) {
        return event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE;
    }
}
