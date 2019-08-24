package edu.stanford.bmir.protege.web.client.crud;

import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.library.dlg.HasInitialFocusable;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKit;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSettings;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 30/07/2013
 */
public interface EntityCrudKitSettingsEditor extends ValueEditor<EntityCrudKitSettings<?>>, HasInitialFocusable {


    void setEntityCrudKits(@Nonnull List<EntityCrudKit<?>> crudKits);
}
