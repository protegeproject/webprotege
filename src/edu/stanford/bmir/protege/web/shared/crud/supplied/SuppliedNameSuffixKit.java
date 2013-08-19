package edu.stanford.bmir.protege.web.shared.crud.supplied;

import edu.stanford.bmir.protege.web.client.crud.supplied.SuppliedSuffixSettingsEditor;
import edu.stanford.bmir.protege.web.server.crud.EntityCrudKitHandler;
import edu.stanford.bmir.protege.web.server.crud.supplied.SuppliedNameSuffixEntityCrudKitHandler;
import edu.stanford.bmir.protege.web.shared.crud.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/08/2013
 */
public class SuppliedNameSuffixKit extends EntityCrudKit<SuppliedNameSuffixSettings> {

    private static final EntityCrudKitId ID = EntityCrudKitId.get("SuppliedNameSuffix");

    private static final SuppliedNameSuffixKit INSTANCE = new SuppliedNameSuffixKit();

    private SuppliedNameSuffixKit() {
        super(ID, "Supplied name");
    }

    public static SuppliedNameSuffixKit get() {
        return INSTANCE;
    }

    @Override
    public EntityCrudKitSuffixSettingsEditor<SuppliedNameSuffixSettings> getSuffixSettingsEditor() {
        return new SuppliedSuffixSettingsEditor();
    }

    @Override
    public EntityCrudKitPrefixSettings getDefaultPrefixSettings() {
        return new EntityCrudKitPrefixSettings();
    }

    @Override
    public SuppliedNameSuffixSettings getDefaultSuffixSettings() {
        return new SuppliedNameSuffixSettings();
    }

}
