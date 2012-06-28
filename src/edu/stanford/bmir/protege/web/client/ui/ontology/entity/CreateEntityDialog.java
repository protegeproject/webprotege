package edu.stanford.bmir.protege.web.client.ui.ontology.entity;

import com.google.gwt.core.client.GWT;
import edu.stanford.bmir.protege.web.client.rpc.OntologyService;
import edu.stanford.bmir.protege.web.client.rpc.OntologyServiceAsync;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogButtonHandler;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogCloser;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/06/2012
 */
public class CreateEntityDialog extends WebProtegeDialog<EntityData> {

    public CreateEntityDialog() {
        super(new CreateEntityDialogController());
        setDialogButtonHandler(DialogButton.OK, new WebProtegeDialogButtonHandler<EntityData>() {
            public void handleHide(EntityData data, WebProtegeDialogCloser closer) {
                OntologyServiceAsync service = GWT.create(OntologyService.class);
                closer.hide();
            }
        });
    }
}
