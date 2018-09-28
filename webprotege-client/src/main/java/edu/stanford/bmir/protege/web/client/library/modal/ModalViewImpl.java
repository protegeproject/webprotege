package edu.stanford.bmir.protege.web.client.library.modal;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27 Sep 2018
 */
public class ModalViewImpl extends Composite implements ModalView {

    private ModalCloser closer = () -> {};

    interface ModalViewImplUiBinder extends UiBinder<HTMLPanel, ModalViewImpl> {

    }

    private static ModalViewImplUiBinder ourUiBinder = GWT.create(ModalViewImplUiBinder.class);

    @UiField
    SimplePanel contentContainer;

    @UiField
    HTMLPanel buttonContainer;

    @UiField
    Label titleField;

    @Nonnull
    private final WebProtegeClientBundle clientBundle;

    private final Map<DialogButton, ModalButtonHandler> handlerMap = new HashMap<>();

    @Inject
    public ModalViewImpl(@Nonnull WebProtegeClientBundle clientBundle) {
        this.clientBundle = checkNotNull(clientBundle);
        initWidget(ourUiBinder.createAndBindUi(this));
    }


    @Override
    public void setCloser(@Nonnull ModalCloser closer) {
        this.closer = checkNotNull(closer);
    }

    @Override
    public void hide() {
        Animation animation = new Animation() {
            @Override
            protected void onUpdate(double progress) {
                getElement().getStyle().setOpacity(1 - progress);
                if(progress == 1.0) {
                    removeFromParent();
                }
            }
        };
        animation.run(300);
    }

    private Button createBasicButton(DialogButton button) {
        Button btn = new Button(button.getButtonName());
        btn.addStyleName(clientBundle.buttons().dialogButton());
        return btn;
    }

    private void installButton(@Nonnull DialogButton button, @Nonnull ModalButtonHandler handler, Button btn) {
        buttonContainer.add(btn);
        handlerMap.put(button, handler);
        btn.addClickHandler(event -> handler.handleModalButton(closer));
    }

    @Override
    public void setModalTitle(@Nonnull String title) {
        titleField.setText(title);
    }

    @Override
    public void addEscapeButton(DialogButton button) {
        Button btn = createBasicButton(button);
        btn.addStyleName(clientBundle.buttons().escapeButton());
        installButton(button, ModalCloser::closeModal, btn);
    }

    @Override
    public void addButton(@Nonnull DialogButton button, @Nonnull ModalButtonHandler handler) {
        Button btn = new Button(button.getButtonName());
        btn.addStyleName(clientBundle.buttons().dialogButton());
        installButton(button, handler, btn);
    }

    @Override
    public void addPrimaryButton(@Nonnull DialogButton button, @Nonnull ModalButtonHandler handler) {
        Button btn = createBasicButton(button);
        btn.addStyleName(clientBundle.buttons().primaryButton());
        installButton(button, handler, btn);
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getModalContainer() {
        return contentContainer;
    }


}