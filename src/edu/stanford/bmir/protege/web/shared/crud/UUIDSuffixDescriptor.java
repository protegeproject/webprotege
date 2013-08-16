package edu.stanford.bmir.protege.web.shared.crud;

import edu.stanford.bmir.protege.web.client.irigen.uuid.UUIDSuffixSettingsEditor;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/08/2013
 */
@EntityCrudKitPlugin
public final class UUIDSuffixDescriptor extends EntityCrudKitDescriptor {

    private static EntityCrudKitId ID = EntityCrudKitId.get("UUID");

    private static final UUIDSuffixDescriptor INSTANCE = new UUIDSuffixDescriptor();

    private UUIDSuffixDescriptor() {
        super(ID, "Auto-generated Universally Unique Id (UUID)");
    }

    public static UUIDSuffixDescriptor get() {
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
