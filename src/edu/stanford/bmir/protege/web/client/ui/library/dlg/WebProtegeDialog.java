package edu.stanford.bmir.protege.web.client.ui.library.dlg;


import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;
import java.util.List;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/01/2012
 */
public class WebProtegeDialog<D> extends DialogBox {

    public static final boolean AUTO_HIDE = false;

    public static final boolean MODAL = true;

    /**
     * The time that should pass between the dialog showing and the default widget requesting the focus.
     */
    private static final int DEFAULT_WIDGET_FOCUS_DELAY = 100;

    private WebProtegeDialogController<D> controller;

//    private List<List<WebProtegeDialogHandler<D>>> buttonHandlers = new ArrayList<List<WebProtegeDialogHandler<D>>>();

    private List<WebProtegeDialogButtonHandler<D>> buttonHandlers = new ArrayList<WebProtegeDialogButtonHandler<D>>();

    public WebProtegeDialog(WebProtegeDialogController<D> controller) {
        super(AUTO_HIDE, MODAL);
        this.controller = controller;

//        setAnimationEnabled(true);
        setGlassEnabled(true);


        setText(controller.getTitle());


        FlowPanel mainPanel = new FlowPanel();
        setWidget(mainPanel);
        addStyleName("web-protege-laf");
        mainPanel.addStyleName("web-protege-dialog-main-panel");

        Widget contentWidget = controller.getWidget();
        SimplePanel contentWidgetWrapper = new SimplePanel();
        contentWidgetWrapper.setWidget(contentWidget);
        mainPanel.add(contentWidgetWrapper);

        Widget buttonBar = createButtonBar();

        mainPanel.add(buttonBar);

        initialiseHandlerList();

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
        buttonBar.setStyleName("web-protege-dialog-button-bar");
        for (DialogButton dlgButton : controller.getButtons()) {
            Button button = dlgButton.createButton();
            button.addClickHandler(new WebProtegeDialogButtonClickHandler(dlgButton));
            buttonBar.add(button);
            button.addStyleName("web-protege-dialog-button");
            button.setWidth("70px");
        }
        SimplePanel buttonBarWrapper = new SimplePanel();
        buttonBarWrapper.setWidget(buttonBar);
        return buttonBarWrapper;
    }

    private void initialiseHandlerList() {
        for (DialogButton button : DialogButton.values()) {
            buttonHandlers.add(new DefaultWebProtegeDialogButtonHandler<D>());
        }
    }

    private void attachAcceleratorKeyHandlers(Widget widget) {
        GWT.log(widget.toString());
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


    public void setDialogButtonHandler(DialogButton button, WebProtegeDialogButtonHandler<D> buttonHandler) {
        buttonHandlers.set(button.ordinal(), buttonHandler);
    }

    @Override
    public void show() {
        super.show();
        setFocusToDefaultWidget();
    }

    /**
     * Sets the focus to the default widget.  I'm not totally sure how to do this, but looking through the code in
     * other places Tania does it by using a timer (although I know she's not happy about this).
     */
    private void setFocusToDefaultWidget() {
        Timer timer = new Timer() {
            @Override
            public void run() {
                Optional<Focusable> initialFocusable = controller.getInitialFocusable();
                if (initialFocusable.isPresent()) {
                    initialFocusable.get().setFocus(true);
                }
            }
        };
        timer.schedule(DEFAULT_WIDGET_FOCUS_DELAY);
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
        WebProtegeDialogButtonHandler<D> buttonHandler = buttonHandlers.get(button.ordinal());
        buttonHandler.handleHide(controller.getData(), new WebProtegeDialogCloser() {
            public void hide() {
                WebProtegeDialog.this.hide();
            }
        });
    }

    private void displayContentsInvalidMessage(String message) {
        Window.alert(message);
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

}
