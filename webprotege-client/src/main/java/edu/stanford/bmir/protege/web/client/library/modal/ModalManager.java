package edu.stanford.bmir.protege.web.client.library.modal;

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

    @Inject
    public ModalManager(@Nonnull Provider<ModalPresenter> modalPresenterProvider) {
        this.modalPresenterProvider = checkNotNull(modalPresenterProvider);
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
//
//        callback.start(getContentContainer());
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
}
