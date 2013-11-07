package edu.stanford.bmir.protege.web.client.ui.library.msgbox;

import com.google.common.base.Optional;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/04/2013
 */
public class InputBox {

    private InputBoxHandler inputBoxHandler = new InputBoxHandler() {
        @Override
        public void handleAcceptInput(String input) {

        }

    };

    public static void showDialog(String title, InputBoxHandler handler) {
        InputBoxController controller = new InputBoxController(title, handler);
        WebProtegeDialog.showDialog(controller);
    }

    private static class InputBoxController extends WebProtegeOKCancelDialogController<String> {

        private InputBoxView view = new InputBoxViewImpl();

        private InputBoxHandler inputBoxHandler;

        private InputBoxController(String title, InputBoxHandler handler) {
            super(title);
            this.inputBoxHandler = handler;
            setDialogButtonHandler(DialogButton.OK, new WebProtegeDialogButtonHandler<String>() {
                @Override
                public void handleHide(String data, WebProtegeDialogCloser closer) {
                    inputBoxHandler.handleAcceptInput(data);
                    closer.hide();
                }
            });
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
