package edu.stanford.bmir.protege.web.client.library.modal;

import com.google.gwt.dom.client.BodyElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27 Sep 2018
 */
public class ModalPresenter implements HasModalButtons {

    private final ModalView view;

    @Inject
    public ModalPresenter(ModalView view) {
        this.view = checkNotNull(view);
        view.setCloser(this::hide);
    }

    public void setTitle(@Nonnull String title) {
        this.view.setModalTitle(title);
    }

    @Override
    public void addEscapeButton(DialogButton button) {
        view.addEscapeButton(button);
    }

    @Override
    public void addButton(@Nonnull DialogButton button, @Nonnull ModalButtonHandler handler) {
        view.addButton(button, handler);
    }

    @Override
    public void addPrimaryButton(@Nonnull DialogButton button, @Nonnull ModalButtonHandler handler) {
        view.addPrimaryButton(button, handler);
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
