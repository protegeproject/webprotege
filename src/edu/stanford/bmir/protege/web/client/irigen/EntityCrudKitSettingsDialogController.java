package edu.stanford.bmir.protege.web.client.irigen;

import com.google.common.base.Optional;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeOKCancelDialogController;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSettings;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSuffixSettings;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 31/07/2013
 */
public class EntityCrudKitSettingsDialogController extends WebProtegeOKCancelDialogController<EntityCrudKitSettings> {

    private static final String TITLE = "New Entity Settings";

    final EntityCrudKitSettingsEditor editor = new EntityCrudKitSettingsEditorImpl();

    public EntityCrudKitSettingsDialogController() {
        super(TITLE);

    }

    public EntityCrudKitSettingsEditor getEditor() {
        return editor;
    }

    @Override
    public Widget getWidget() {
        return editor.asWidget();
    }

    @Override
    public Optional<Focusable> getInitialFocusable() {
        return editor.getInitialFocusable();
    }

    @Override
    public EntityCrudKitSettings getData() {
        return editor.getValue().get();
    }
}
