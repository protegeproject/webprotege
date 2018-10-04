package edu.stanford.bmir.protege.web.client.library.dlg;


import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.ui.*;
import com.google.web.bindery.event.shared.HandlerRegistration;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle.BUNDLE;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/01/2012
 */
public final class WebProtegeDialog<D> extends DialogBox {

    public static final boolean AUTO_HIDE = false;

    public static final boolean MODAL = true;

    private static final String WEB_PROTEGE_DIALOG_MAIN_PANEL = "web-protege-dialog";

    private static final String WEB_PROTEGE_DIALOG_BUTTON_BAR = "web-protege-dialog-button-bar";

    private final ArrayList<HandlerRegistration> handlerRegistrations;

    private WebProtegeDialogController<D> controller;

    private final FlowPanel mainPanel;

    public WebProtegeDialog(WebProtegeDialogController<D> controller) {
        super(AUTO_HIDE, MODAL);
        this.controller = controller;

        setGlassEnabled(true);


        setText(controller.getTitle());
        mainPanel = new FlowPanel();
        setWidget(mainPanel);
        mainPanel.addStyleName(WEB_PROTEGE_DIALOG_MAIN_PANEL);

        Widget contentWidget = controller.getWidget();
        SimplePanel contentWidgetWrapper = new SimplePanel();
        contentWidgetWrapper.setWidget(contentWidget);
        mainPanel.add(contentWidgetWrapper);

        Widget buttonBar = createButtonBar();

        mainPanel.add(buttonBar);

        handlerRegistrations = new ArrayList<>();
        attachAcceleratorKeyHandlers(contentWidget, handlerRegistrations);

        center();
    }

    @Override
    public void setVisible(boolean visible) {
        setText(controller.getTitle());
        super.setVisible(visible);
    }

    public WebProtegeDialogController<D> getController() {
        return controller;
    }

    private Widget createButtonBar() {
        FlowPanel buttonBar = new FlowPanel();
        buttonBar.setStyleName(WEB_PROTEGE_DIALOG_BUTTON_BAR);
        for (DialogButton dlgButton : controller.getButtons()) {
            Button button = dlgButton.createButton();
            button.addClickHandler(new WebProtegeDialogButtonClickHandler(dlgButton));
            buttonBar.add(button);
            // Base style for buttons
            button.addStyleName(BUNDLE.buttons().button());
            // Style for settings buttons
            button.addStyleName(BUNDLE.buttons().dialogButton());
            if(dlgButton.equals(controller.getDefaultButton())) {
                button.addStyleName(BUNDLE.buttons().acceptButton());
            }
            else {
                button.addStyleName(BUNDLE.buttons().escapeButton());
            }
        }
        SimplePanel buttonBarWrapper = new SimplePanel();
        buttonBarWrapper.setWidget(buttonBar);
        return buttonBarWrapper;
    }



    private void attachAcceleratorKeyHandlers(Widget widget, List<HandlerRegistration> handlerRegistrations) {
        if(widget instanceof HasAcceptKeyHandler) {
            ((HasAcceptKeyHandler) widget).setAcceptKeyHandler(this::hideWithDefaultButton);
        }
        else {
            if (widget instanceof TextArea) {
                handlerRegistrations.add(((TextArea) widget).addKeyDownHandler(new CtrlEnterKeyDownHandler()));
            }
            else if (widget instanceof TextBox) {
                handlerRegistrations.add(((HasKeyDownHandlers) widget).addKeyDownHandler(new EnterKeyDownHandler()));
            }
        }
        if(widget instanceof HasKeyDownHandlers) {
            handlerRegistrations.add(((HasKeyDownHandlers) widget).addKeyDownHandler(new EscapeKeyDownHandler()));
        }


        if (widget instanceof HasWidgets) {
            HasWidgets hasWidgets = (HasWidgets) widget;
            for (Widget childWidget : hasWidgets) {
                attachAcceleratorKeyHandlers(childWidget, handlerRegistrations);
            }
        }
        else if(widget instanceof HasOneWidget) {
            attachAcceleratorKeyHandlers(((HasOneWidget) widget).getWidget(), handlerRegistrations);
        }
    }

    @Override
    public void hide(boolean autoClosed) {
        super.hide(autoClosed);
        handlerRegistrations.forEach(HandlerRegistration::removeHandler);
    }

    @Override
    public void hide() {
        super.hide();
        handlerRegistrations.forEach(HandlerRegistration::removeHandler);
    }

    @Override
    public void show() {
        setTitle(controller.getTitle());
        super.show();
        setFocusToDefaultWidget();
    }

    private void setFocusToDefaultWidget() {
        Scheduler.get().scheduleDeferred(() -> {
            controller.getInitialFocusable().ifPresent(HasRequestFocus::requestFocus);
        });
    }


    private void hideWithButton(DialogButton button) {
        if (!button.equals(DialogButton.CANCEL)) {
            for (WebProtegeDialogValidator validator : controller.getDialogValidators()) {
                if (validator.getValidationState().equals(ValidationState.INVALID)) {
                    displayContentsInvalidMessage(validator.getValidationMessage());
                    return;
                }
            }
        }
        controller.getButtonHandler(button).ifPresent(buttonHandler -> {
            buttonHandler.handleHide(controller.getData(), WebProtegeDialog.this::hide);
        });
    }

    private void displayContentsInvalidMessage(String message) {
    }

    private void hideWithDefaultButton() {
        hideWithButton(controller.getDefaultButton());
    }

    private void hideWithCancelButton() {
        hideWithButton(controller.getEscapeButton());
    }


    private class WebProtegeDialogButtonClickHandler implements ClickHandler {

        private DialogButton button;

        private WebProtegeDialogButtonClickHandler(DialogButton button) {
            this.button = button;
        }

        public void onClick(ClickEvent event) {
            hideWithButton(button);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////
    ////  Handlers for keyboard shortcuts/accelerators
    ////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private class EnterKeyDownHandler implements KeyDownHandler {

        public void onKeyDown(KeyDownEvent event) {
            if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                hideWithDefaultButton();
            }
        }
    }

    private class CtrlEnterKeyDownHandler implements KeyDownHandler {

        public void onKeyDown(KeyDownEvent event) {
            if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && event.isControlKeyDown()) {
                GWT.log("[WebProtegeDialog] CTRL+ENTER Accelerator pressed");
                hideWithDefaultButton();
            }
        }
    }

    private class EscapeKeyDownHandler implements KeyDownHandler {

        public void onKeyDown(KeyDownEvent event) {
            if (event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE) {
                hideWithCancelButton();
            }
        }
    }

    /**
     * Shows a settings for the specified controller.
     * @param controller The controller for which a settings should be shown.  Not {@code null}.
     * @param <D> The type of data shown by the settings.
     * @throws NullPointerException if {@code controller} is {@code null}.
     */
    public static <D> void showDialog(WebProtegeDialogController<D> controller) {
        WebProtegeDialog<D> dlg = new WebProtegeDialog<D>(checkNotNull(controller));
        dlg.show();
    }

}
