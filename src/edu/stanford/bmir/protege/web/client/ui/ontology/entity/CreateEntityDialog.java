package edu.stanford.bmir.protege.web.client.ui.ontology.entity;

import com.google.gwt.core.client.GWT;
import edu.stanford.bmir.protege.web.client.rpc.OntologyService;
import edu.stanford.bmir.protege.web.client.rpc.OntologyServiceAsync;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogButtonHandler;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogCloser;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/06/2012
 */
public class CreateEntityDialog extends WebProtegeDialog<CreateEntityInfo> {

    public CreateEntityDialog(EntityType<?> entityType) {
        super(new CreateEntityDialogController(entityType));
        setDialogButtonHandler(DialogButton.OK, new WebProtegeDialogButtonHandler<CreateEntityInfo>() {
            public void handleHide(CreateEntityInfo data, WebProtegeDialogCloser closer) {
                closer.hide();
            }
        });
    }
}
