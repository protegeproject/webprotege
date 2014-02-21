package edu.stanford.bmir.protege.web.client.ui.obo;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.widgets.MessageBox;
import edu.stanford.bmir.protege.web.client.rpc.data.NotSignedInException;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/05/2012
 */
public class OBOTermEditorApplyChangesAsyncCallback implements AsyncCallback<Void> {

    private String msg;

    public OBOTermEditorApplyChangesAsyncCallback() {
        msg = "Your changes have not been applied";
    }

    public OBOTermEditorApplyChangesAsyncCallback(String msg) {
        this.msg = msg;
    }

    public void onFailure(Throwable caught) {
        if(caught instanceof NotSignedInException) {
            MessageBox.alert(msg + ".  You must be signed in for your changes to be applied.");
        }
    }

    public void onSuccess(Void result) {
        // Do nothing
    }
}
