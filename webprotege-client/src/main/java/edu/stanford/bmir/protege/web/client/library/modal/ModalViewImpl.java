package edu.stanford.bmir.protege.web.client.library.modal;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;

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
public class ModalViewImpl extends Composite implements ModalView {

    @Nonnull
    private Optional<Button> primaryButton = Optional.empty();

    private boolean primaryButtonFocusedOnAttach = false;

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

    @Inject
    public ModalViewImpl(@Nonnull WebProtegeClientBundle clientBundle) {
        this.clientBundle = checkNotNull(clientBundle);
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        if(primaryButtonFocusedOnAttach) {
            primaryButton.ifPresent(btn -> btn.setFocus(true));
        }
    }

    @Override
    public void hide() {
        Animation animation = new Animation() {
            @Override
            protected void onUpdate(double progress) {
                getElement().getStyle().setOpacity(1 - progress);
                if(progress == 1.0) {
                    removeFromParent();
                    getElement().getStyle().setOpacity(1);
                }
            }
        };
        animation.run(300);
    }

    @Override
    public void setPrimaryButtonFocusedOnAttach(boolean focused) {
        this.primaryButtonFocusedOnAttach = focused;
        if(isAttached()) {
            primaryButton.ifPresent(btn -> btn.setFocus(true));
        }
    }

    private Button createBasicButton(DialogButton button) {
        Button btn = new Button(button.getButtonName());
        btn.addStyleName(clientBundle.buttons().dialogButton());
        return btn;
    }

    private void installButton(@Nonnull DialogButton button, @Nonnull Runnable handler, Button btn) {
        buttonContainer.add(btn);
        btn.addClickHandler(event -> handler.run());
    }

    @Override
    public void setCaption(@Nonnull String title) {
        titleField.setText(title);
    }

    @Override
    public void setEscapeButton(DialogButton button,  @Nonnull Runnable handler) {
        Button btn = createBasicButton(button);
        btn.addStyleName(clientBundle.buttons().escapeButton());
        installButton(button, handler, btn);
    }

    @Override
    public void addButton(@Nonnull DialogButton button, @Nonnull Runnable handler) {
        Button btn = new Button(button.getButtonName());
        btn.addStyleName(clientBundle.buttons().dialogButton());
        installButton(button, handler, btn);
    }

    @Override
    public void setPrimaryButton(@Nonnull DialogButton button, @Nonnull Runnable handler) {
        Button btn = createBasicButton(button);
        btn.addStyleName(clientBundle.buttons().primaryButton());
        installButton(button, handler, btn);
        this.primaryButton = Optional.of(btn);
    }

    @Override
    public void setContent(@Nonnull IsWidget content) {
        contentContainer.setWidget(checkNotNull(content));
    }
}