package edu.stanford.bmir.protege.web.client.crud;

import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeOKCancelDialogController;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSettings;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 31/07/2013
 */
public class EntityCrudKitSettingsDialogController extends WebProtegeOKCancelDialogController<EntityCrudKitSettings<?>> {

    private static final String TITLE = "New Entity Settings";

    private final EntityCrudKitSettingsEditor editor;

    @Inject
    public EntityCrudKitSettingsDialogController(EntityCrudKitSettingsEditor editor) {
        super(TITLE);
        this.editor = editor;
    }

    public EntityCrudKitSettingsEditor getEditor() {
        return editor;
    }

    @Override
    public Widget getWidget() {
        return editor.asWidget();
    }

    @Nonnull
    @Override
    public java.util.Optional<HasRequestFocus> getInitialFocusable() {
        return editor.getInitialFocusable();
    }

    @Override
    public EntityCrudKitSettings<?> getData() {
        return editor.getValue().get();
    }
}
