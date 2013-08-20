package edu.stanford.bmir.protege.web.shared.crud.oboid;

import edu.stanford.bmir.protege.web.client.crud.obo.OBOIdSuffixSettingsEditor;
import edu.stanford.bmir.protege.web.server.crud.EntityCrudKitHandler;
import edu.stanford.bmir.protege.web.server.crud.supplied.SuppliedNameSuffixEntityCrudKitHandler;
import edu.stanford.bmir.protege.web.shared.crud.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/08/2013
 */
public class OBOIdSuffixKit extends EntityCrudKit<OBOIdSuffixSettings> {

    private static final OBOIdSuffixKit INSTANCE = new OBOIdSuffixKit();


    private OBOIdSuffixKit() {
        super(EntityCrudKitId.get("OBO"), "Auto-generated  OBO Style Id");
    }

    public static OBOIdSuffixKit get() {
        return INSTANCE;
    }

    @Override
    public EntityCrudKitSuffixSettingsEditor<OBOIdSuffixSettings> getSuffixSettingsEditor() {
        return new OBOIdSuffixSettingsEditor();
    }

    @Override
    public EntityCrudKitPrefixSettings getDefaultPrefixSettings() {
        return new EntityCrudKitPrefixSettings("http://purl.obolibrary.org/obo/");
    }

    @Override
    public OBOIdSuffixSettings getDefaultSuffixSettings() {
        return new OBOIdSuffixSettings();
    }

}
