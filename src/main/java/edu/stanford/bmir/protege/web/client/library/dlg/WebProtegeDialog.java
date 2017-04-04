package edu.stanford.bmir.protege.web.client.library.dlg;


import com.google.common.base.Optional;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/01/2012
 */
public final class WebProtegeDialog<D> extends DialogBox {

    public static final boolean AUTO_HIDE = false;

    public static final boolean MODAL = true;

    private static final String WEB_PROTEGE_LAF = "web-protege-laf";

    private static final String WEB_PROTEGE_DIALOG_MAIN_PANEL = "web-protege-dialog-main-panel";

    private static final String WEB_PROTEGE_DIALOG_BUTTON_BAR = "web-protege-dialog-button-bar";

    private WebProtegeDialogController<D> controller;

    private final FlowPanel mainPanel;

    public WebProtegeDialog(WebProtegeDialogController<D> controller) {
        super(AUTO_HIDE, MODAL);
        this.controller = controller;

        setGlassEnabled(true);


        setText(controller.getTitle());
        mainPanel = new FlowPanel();
        setWidget(mainPanel);
        addStyleName(WEB_PROTEGE_LAF);
        mainPanel.addStyleName(WEB_PROTEGE_DIALOG_MAIN_PANEL);

        Widget contentWidget = controller.getWidget();
        SimplePanel contentWidgetWrapper = new SimplePanel();
        contentWidgetWrapper.setWidget(contentWidget);
        mainPanel.add(contentWidgetWrapper);

        Widget buttonBar = createButtonBar();

        mainPanel.add(buttonBar);

        attachAcceleratorKeyHandlers(contentWidget);

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
            button.addStyleName("button-style");
            button.setWidth("70px");
        }
        SimplePanel buttonBarWrapper = new SimplePanel();
        buttonBarWrapper.setWidget(buttonBar);
        return buttonBarWrapper;
    }



    private void attachAcceleratorKeyHandlers(Widget widget) {
        if(widget instanceof HasAcceptKeyHandler) {
            ((HasAcceptKeyHandler) widget).setAcceptKeyHandler(new AcceptKeyHandler() {
                @Override
                public void handleAcceptKey() {
                    hideWithDefaultButton();
                }
            });
        }
        else {
            if (widget instanceof TextArea) {
                ((TextArea) widget).addKeyDownHandler(new CtrlEnterKeyDownHandler());
            }
            else if (widget instanceof TextBox) {
                ((HasKeyDownHandlers) widget).addKeyDownHandler(new EnterKeyDownHandler());
            }
        }
        if(widget instanceof HasKeyDownHandlers) {
            ((HasKeyDownHandlers) widget).addKeyDownHandler(new EscapeKeyDownHandler());
        }



        if (widget instanceof HasWidgets) {
            HasWidgets hasWidgets = (HasWidgets) widget;
            for (Widget childWidget : hasWidgets) {
                attachAcceleratorKeyHandlers(childWidget);
            }
        }
    }



    @Override
    public void show() {
        setTitle(controller.getTitle());
        super.show();
        setFocusToDefaultWidget();
    }

    private void setFocusToDefaultWidget() {
        Scheduler.get().scheduleDeferred(() -> {
            controller.getInitialFocusable().ifPresent(focusable -> focusable.setFocus(true));
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
        WebProtegeDialogButtonHandler<D> buttonHandler = controller.getButtonHandlers().get(button.ordinal());
        buttonHandler.handleHide(controller.getData(), new WebProtegeDialogCloser() {
            public void hide() {
                WebProtegeDialog.this.hide();
            }
        });
    }

    private void displayContentsInvalidMessage(String message) {
        MessageBox.showAlert(message);
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
     * Shows a dialog for the specified controller.
     * @param controller The controller for which a dialog should be shown.  Not {@code null}.
     * @param <D> The type of data shown by the dialog.
     * @throws NullPointerException if {@code controller} is {@code null}.
     */
    public static <D> void showDialog(WebProtegeDialogController<D> controller) {
        WebProtegeDialog<D> dlg = new WebProtegeDialog<D>(checkNotNull(controller));
        dlg.setVisible(true);
    }

}
