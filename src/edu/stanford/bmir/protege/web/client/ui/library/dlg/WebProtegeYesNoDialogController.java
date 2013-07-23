package edu.stanford.bmir.protege.web.client.ui.library.dlg;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/07/2013
 */
public abstract  class WebProtegeYesNoDialogController<D> extends WebProtegeDialogController<D> {

    public static final List<DialogButton> BUTTONS = Collections.unmodifiableList(Arrays.asList(DialogButton.YES, DialogButton.NO));

    public static final DialogButton DEFAULT_BUTTON = DialogButton.YES;

    public static final DialogButton CANCEL_BUTTON = DialogButton.NO;

    protected WebProtegeYesNoDialogController(String title) {
        super(title, BUTTONS, DEFAULT_BUTTON, CANCEL_BUTTON);
    }
}
