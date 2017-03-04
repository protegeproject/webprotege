package edu.stanford.bmir.protege.web.client.library.dlg;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/01/2012
 */
public abstract class WebProtegeOKCancelDialogController<D> extends WebProtegeDialogController<D> {

    public static final List<DialogButton> BUTTONS = Collections.unmodifiableList(Arrays.asList(DialogButton.OK, DialogButton.CANCEL));

    public static final DialogButton DEFAULT_BUTTON = DialogButton.OK;

    public static final DialogButton CANCEL_BUTTON = DialogButton.CANCEL;

    protected WebProtegeOKCancelDialogController(String title) {
        super(title, BUTTONS, DEFAULT_BUTTON, CANCEL_BUTTON);
    }
}
