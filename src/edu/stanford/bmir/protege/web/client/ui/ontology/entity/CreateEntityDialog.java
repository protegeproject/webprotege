package edu.stanford.bmir.protege.web.client.ui.ontology.entity;

import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialog;
import org.semanticweb.owlapi.model.EntityType;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/06/2012
 */
public class CreateEntityDialog extends WebProtegeDialog<CreateEntityInfo> {

    public CreateEntityDialog(EntityType<?> entityType, CreateEntityDialogController.CreateEntityHandler handler) {
        super(new CreateEntityDialogController(entityType, handler));
    }


}
