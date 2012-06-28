package edu.stanford.bmir.protege.web.client.ui.library.dlg;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/01/2012
 */
public interface WebProtegeDialogButtonHandler<D> {

    void handleHide(D data, WebProtegeDialogCloser closer);
}
