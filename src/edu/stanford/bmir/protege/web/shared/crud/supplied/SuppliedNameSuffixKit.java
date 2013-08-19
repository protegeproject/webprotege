package edu.stanford.bmir.protege.web.shared.crud.supplied;

import edu.stanford.bmir.protege.web.client.crud.supplied.SuppliedSuffixSettingsEditor;
import edu.stanford.bmir.protege.web.shared.crud.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/08/2013
 */
@EntityCrudKitPlugin
public class SuppliedNameSuffixKit extends EntityCrudKit {

    private static final EntityCrudKitId ID = EntityCrudKitId.get("SuppliedNameSuffix");

    private static final SuppliedNameSuffixKit INSTANCE = new SuppliedNameSuffixKit();

    private SuppliedNameSuffixKit() {
        super(ID, "Supplied name");
    }

    public static SuppliedNameSuffixKit get() {
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
