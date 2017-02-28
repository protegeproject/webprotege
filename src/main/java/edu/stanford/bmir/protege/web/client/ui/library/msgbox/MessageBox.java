package edu.stanford.bmir.protege.web.client.ui.library.msgbox;

import com.google.common.base.Optional;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/07/2013
 */
public class MessageBox {

    private static final Void RETURN = null;

    private static final String DLG_TITLE = "";

    private static final String DEFAULT_SUB_MESSAGE = "";



    public static void showMessage(String mainMessage) {
        showMessage(mainMessage, DEFAULT_SUB_MESSAGE);
    }

//    public static void showPlainMessage(String mainMessage, String subMessage) {
//        showMessageBox(MessageStyle.PLAIN, mainMessage, subMessage);
//    }

    public static void showMessage(String mainMessage, String subMessage) {
        showMessageBox(MessageStyle.MESSAGE, mainMessage, subMessage);
    }

    public static void showAlert(String mainMessage) {
        showAlert(mainMessage, "");
    }

    public static void showAlert(String mainMessage, String subMessage) {
        showMessageBox(MessageStyle.ALERT, mainMessage, subMessage);
    }

    public static void showErrorMessage(String mainMessage, Throwable throwable) {
        SafeHtmlBuilder sb = new SafeHtmlBuilder();
        sb.appendEscaped("Details: ");
        sb.appendEscaped(throwable.getMessage());
        showAlert(mainMessage, sb.toSafeHtml().toString());
    }


    public static void showOKCancelConfirmBox(String mainMessage, String subMessage, final OKCancelHandler handler) {
        final MessageBoxView messageBoxView = createMessageBox(MessageStyle.QUESTION, mainMessage, subMessage);
        final WebProtegeOKCancelDialogController<Void> controller = new WebProtegeOKCancelDialogController<Void>(DLG_TITLE) {
            @Override
            public Widget getWidget() {
                return messageBoxView.asWidget();
            }

            @Override
            public Optional<Focusable> getInitialFocusable() {
                return Optional.absent();
            }

            @Override
            public Void getData() {
                return RETURN;
            }
        };
        controller.setDialogButtonHandler(DialogButton.OK, new WebProtegeDialogButtonHandler<Void>() {
            @Override
            public void handleHide(Void data, WebProtegeDialogCloser closer) {
                closer.hide();
                handler.handleOK();
            }
        });
        controller.setDialogButtonHandler(DialogButton.CANCEL, new WebProtegeDialogButtonHandler<Void>() {
            @Override
            public void handleHide(Void data, WebProtegeDialogCloser closer) {
                closer.hide();
                handler.handleCancel();
            }
        });
        WebProtegeDialog<Void> dlg = createDialog(controller);
        dlg.setVisible(true);
        scheduleCentering(dlg);
    }

    public static void showYesNoConfirmBox(String mainMessage, String subMessage, final YesNoHandler handler) {
        final MessageBoxView messageBoxView = createMessageBox(MessageStyle.QUESTION, mainMessage, subMessage);
        final WebProtegeYesNoDialogController<Void> controller = new WebProtegeYesNoDialogController<Void>(DLG_TITLE) {
            @Override
            public Widget getWidget() {
                return messageBoxView.asWidget();
            }

            @Override
            public Optional<Focusable> getInitialFocusable() {
                return Optional.absent();
            }

            @Override
            public Void getData() {
                return RETURN;
            }
        };
        controller.setDialogButtonHandler(DialogButton.YES, new WebProtegeDialogButtonHandler<Void>() {
            @Override
            public void handleHide(Void data, WebProtegeDialogCloser closer) {
                closer.hide();
                handler.handleYes();
            }
        });
        controller.setDialogButtonHandler(DialogButton.NO, new WebProtegeDialogButtonHandler<Void>() {
            @Override
            public void handleHide(Void data, WebProtegeDialogCloser closer) {
                closer.hide();
                handler.handleNo();

            }
        });
        WebProtegeDialog<Void> dlg = createDialog(controller);
        dlg.show();
        scheduleCentering(dlg);
    }

    public static void showYesNoConfirmBox(String mainMessage, String subMessage, final Runnable yesHandler) {
        showYesNoConfirmBox(mainMessage, subMessage, new YesNoHandler() {
            @Override
            public void handleYes() {
                yesHandler.run();
            }

            @Override
            public void handleNo() {
                // Ignore
            }
        });
    }


    private static void showMessageBox(MessageStyle messageStyle, String mainMessage, String subMessage) {
        final MessageBoxView messageBoxView = createMessageBox(messageStyle, mainMessage, subMessage);
        final WebProtegeOKDialogController<Void> controller = new WebProtegeOKDialogController<Void>(DLG_TITLE) {
            @Override
            public Widget getWidget() {
                return messageBoxView.asWidget();
            }

            @Override
            public Optional<Focusable> getInitialFocusable() {
                return Optional.absent();
            }

            @Override
            public Void getData() {
                return RETURN;
            }
        };
        final WebProtegeDialog<Void> dlg = createDialog(controller);
        dlg.setVisible(true);
        scheduleCentering(dlg);
    }

    private static void scheduleCentering(final WebProtegeDialog<Void> dlg) {
        Scheduler.get().scheduleDeferred(() -> {
            int left = (Window.getClientWidth() - dlg.getOffsetWidth()) / 2;
            int top = (Window.getClientHeight() - dlg.getOffsetHeight()) / 2;
            dlg.setPopupPosition(left, top);
            dlg.setVisible(true);
        });
    }

    private static MessageBoxView createMessageBox(MessageStyle messageStyle, String mainMessage, String subMessage) {
        final MessageBoxView messageBoxView = new MessageBoxViewImpl();
        messageBoxView.setMainMessage(mainMessage);
        messageBoxView.setSubMessage(subMessage);
        messageBoxView.setMessageStyle(messageStyle);
        return messageBoxView;
    }

    private static WebProtegeDialog<Void> createDialog(WebProtegeDialogController<Void> controller) {
        WebProtegeDialog<Void> dlg = new WebProtegeDialog<Void>(controller);
        dlg.setGlassEnabled(true);
        dlg.setGlassStyleName("glass");
        dlg.addStyleName("glass-popup-shadow");
        return dlg;
    }


}
