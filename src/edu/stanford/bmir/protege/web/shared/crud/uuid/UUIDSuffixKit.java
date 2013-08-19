package edu.stanford.bmir.protege.web.shared.crud.uuid;

import edu.stanford.bmir.protege.web.client.crud.uuid.UUIDSuffixSettingsEditor;
import edu.stanford.bmir.protege.web.server.crud.EntityCrudKitHandler;
import edu.stanford.bmir.protege.web.server.crud.uuid.UUIDEntityCrudKitHandler;
import edu.stanford.bmir.protege.web.shared.crud.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/08/2013
 */
public final class UUIDSuffixKit extends EntityCrudKit {

    private static EntityCrudKitId ID = EntityCrudKitId.get("UUID");

    private static final UUIDSuffixKit INSTANCE = new UUIDSuffixKit();

    private UUIDSuffixKit() {
        super(ID, "Auto-generated Universally Unique Id (UUID)");
    }

    public static UUIDSuffixKit get() {
        return INSTANCE;
    }

    @Override
    public EntityCrudKitSuffixSettingsEditor<?> getSuffixSettingsEditor() {
        return new UUIDSuffixSettingsEditor();
    }

    @Override
    public EntityCrudKitPrefixSettings getDefaultPrefixSettings() {
        return new EntityCrudKitPrefixSettings();
    }

    @Override
    public EntityCrudKitSuffixSettings getDefaultSuffixSettings() {
        return new UUIDSuffixSettings();
    }
}
