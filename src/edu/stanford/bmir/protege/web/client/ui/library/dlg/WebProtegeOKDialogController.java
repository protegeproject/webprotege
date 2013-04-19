package edu.stanford.bmir.protege.web.client.ui.library.dlg;

import java.util.Arrays;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/04/2013
 */
public abstract class WebProtegeOKDialogController<D> extends WebProtegeDialogController<D> {

    public WebProtegeOKDialogController(String title) {
        super(title, Arrays.asList(DialogButton.OK), DialogButton.OK, DialogButton.OK);
    }
}
