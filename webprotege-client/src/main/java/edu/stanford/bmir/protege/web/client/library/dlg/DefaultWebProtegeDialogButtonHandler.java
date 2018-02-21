package edu.stanford.bmir.protege.web.client.library.dlg;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/01/2012
 */
public class DefaultWebProtegeDialogButtonHandler<D> implements WebProtegeDialogButtonHandler<D> {

    public void handleHide(D data, WebProtegeDialogCloser closer) {
        closer.hide();
    }
}
