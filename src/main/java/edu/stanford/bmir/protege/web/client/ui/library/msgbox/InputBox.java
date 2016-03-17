package edu.stanford.bmir.protege.web.client.ui.library.msgbox;

import com.google.common.base.Optional;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.*;

import java.util.Arrays;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/04/2013
 */
public class InputBox {

    public static void showDialog(String title, InputBoxHandler handler) {
        showDialog(title, true, "", handler);
    }

    public static void showDialog(String title, boolean multiline, String initialInput, InputBoxHandler handler) {
        InputBoxController controller = new InputBoxController(title, initialInput, handler, Arrays.asList(DialogButton.OK, DialogButton.CANCEL));
        controller.setMultiline(multiline);
        WebProtegeDialog.showDialog(controller);
    }

    public static void showOkDialog(String title, boolean multiline, String initialInput, InputBoxHandler handler) {
        InputBoxController controller = new InputBoxController(title, initialInput, handler, Arrays.asList(DialogButton.OK));
        controller.setMultiline(multiline);
        WebProtegeDialog.showDialog(controller);
    }

    private static class InputBoxController extends WebProtegeDialogController<String> {

        private InputBoxView view = new InputBoxViewImpl();

        private InputBoxHandler inputBoxHandler;

        private InputBoxController(String title, String initialInput, InputBoxHandler handler, List<DialogButton> buttonList) {
            super(title, buttonList, buttonList.get(0), DialogButton.CANCEL);
            this.inputBoxHandler = handler;
            view.setInitialInput(initialInput);
            setDialogButtonHandler(DialogButton.OK, new WebProtegeDialogButtonHandler<String>() {
                @Override
                public void handleHide(String data, WebProtegeDialogCloser closer) {
                    inputBoxHandler.handleAcceptInput(data);
                    closer.hide();
                }
            });
        }

        public void setMultiline(boolean multiline) {
            view.setMultiline(multiline);
        }

        @Override
        public Widget getWidget() {
            return view.getWidget();
        }

        @Override
        public Optional<Focusable> getInitialFocusable() {
            return view.getInitialFocusable();
        }

        @Override
        public String getData() {
            return view.getInputValue();
        }
    }
}
