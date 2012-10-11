package edu.stanford.bmir.protege.web.client.ui.projectconfig;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.rpc.EntityIdGeneratorSettingsService;
import edu.stanford.bmir.protege.web.client.rpc.EntityIdGeneratorSettingsServiceAsync;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityIdGeneratorSettings;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectId;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogButtonHandler;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogCloser;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 07/10/2012
 */
public class EntityIdGeneratorSettingsDialog extends WebProtegeDialog<EntityIdGeneratorSettings> {


    public EntityIdGeneratorSettingsDialog(final ProjectId projectId) {
        super(new EntityGeneratorSettingsDialogController(projectId));
        setDialogButtonHandler(DialogButton.OK, new WebProtegeDialogButtonHandler<EntityIdGeneratorSettings>() {
            public void handleHide(EntityIdGeneratorSettings data, WebProtegeDialogCloser closer) {
                closer.hide();
                EntityIdGeneratorSettingsServiceAsync service = GWT.create(EntityIdGeneratorSettingsService.class);
                service.setSettings(projectId, data, new AsyncCallback<Void>() {
                    public void onFailure(Throwable caught) {
                    }

                    public void onSuccess(Void result) {
                    }
                });
            }
        });

    }

}
