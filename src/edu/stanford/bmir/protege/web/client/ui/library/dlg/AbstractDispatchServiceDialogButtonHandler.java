package edu.stanford.bmir.protege.web.client.ui.library.dlg;

import com.google.common.base.Optional;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 07/08/2013
 */
public abstract class AbstractDispatchServiceDialogButtonHandler<D, A extends Action<R>, R extends Result> implements WebProtegeDialogButtonHandler<D> {


    protected abstract A createAction(D data);


    @Override
    public void handleHide(D data, final WebProtegeDialogCloser closer) {
        DispatchServiceManager.get().execute(createAction(data), new AsyncCallback<R>() {
            @Override
            public void onFailure(Throwable caught) {
                handleFailure(caught, closer);
            }

            @Override
            public void onSuccess(R result) {
                handleSuccess(result, closer);
            }
        });
    }


    private void handleFailure(Throwable caught, WebProtegeDialogCloser closer) {
        GWT.log("Error while dispatching action", caught);
        Optional<String> errorMessage = getErrorMessage(caught);
        if(errorMessage.isPresent()) {
            MessageBox.showErrorMessage(errorMessage.get(), caught);
        }
        DialogAction errorStrategy = handleFailure(caught);
        if(errorStrategy == DialogAction.HIDE_DIALOG) {
            closer.hide();
        }
    }

    /**
     * Gets a message to be displayed to the user when an error occurs.  Subclasses can override this method to return
     * an error message appropriate to them.
     * @param throwable The error that occurred.
     * @return An optional message to display to the user.  By default this message is not present.  Not {@code null}.
     */
    protected Optional<String> getErrorMessage(Throwable throwable) {
        return Optional.absent();
    }

    private void handleSuccess(R result, WebProtegeDialogCloser closer) {
        closer.hide();
        handleSuccess(result);
    }

    protected void handleSuccess(R result) {

    }

    protected DialogAction handleFailure(Throwable throwable) {
        return DialogAction.HIDE_DIALOG;
    }

    public static enum DialogAction {

        HIDE_DIALOG,

        KEEP_DIALOG_VISIBLE

    }



}
