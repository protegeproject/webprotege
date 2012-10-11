package edu.stanford.bmir.protege.web.client.ui.projectconfig;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.rpc.EntityIdGeneratorSettingsService;
import edu.stanford.bmir.protege.web.client.rpc.EntityIdGeneratorSettingsServiceAsync;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityIdGeneratorSettings;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectId;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeOKCancelDialogController;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 07/10/2012
 */
public class EntityGeneratorSettingsDialogController extends WebProtegeOKCancelDialogController<EntityIdGeneratorSettings> {

    private final EntityIdGeneratorSettingsForm form;
    
    public EntityGeneratorSettingsDialogController(ProjectId projectId) {
        super("Entity Id Generator Settings");
        form = new EntityIdGeneratorSettingsForm();
        EntityIdGeneratorSettingsServiceAsync service = GWT.create(EntityIdGeneratorSettingsService.class);
        service.getSettings(projectId, new AsyncCallback<EntityIdGeneratorSettings>() {
            public void onFailure(Throwable caught) {
                GWT.log("Problem getting id settings", caught);
            }

            public void onSuccess(EntityIdGeneratorSettings result) {
                form.setData(result);
            }
        });
    }

    @Override
    public Widget getWidget() {
        return form;
    }

    @Override
    public Focusable getInitialFocusable() {
        return form.getInitialFocusable();
    }

    @Override
    public EntityIdGeneratorSettings getData() {
        return form.getData();
    }
}
