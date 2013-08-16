package edu.stanford.bmir.protege.web.shared.crud;

import edu.stanford.bmir.protege.web.client.irigen.supplied.SuppliedSuffixSettingsEditor;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/08/2013
 */
@EntityCrudKitPlugin
public class SuppliedNameSuffixDescriptor extends EntityCrudKitDescriptor {

    private static final EntityCrudKitId ID = EntityCrudKitId.get("SuppliedNameSuffix");

    private static final SuppliedNameSuffixDescriptor INSTANCE = new SuppliedNameSuffixDescriptor();

    private SuppliedNameSuffixDescriptor() {
        super(ID, "Supplied name");
    }

    public static SuppliedNameSuffixDescriptor get() {
        return INSTANCE;
    }

    @Override
    public EntityCrudKitSuffixSettingsEditor<?> getSuffixSettingsEditor() {
        return new SuppliedSuffixSettingsEditor();
    }

    @Override
    public EntityCrudKitPrefixSettings getDefaultPrefixSettings() {
        return new EntityCrudKitPrefixSettings();
    }

    @Override
    public EntityCrudKitSuffixSettings getDefaultSuffixSettings() {
        return new SuppliedNameSuffixSettings();
    }
}
