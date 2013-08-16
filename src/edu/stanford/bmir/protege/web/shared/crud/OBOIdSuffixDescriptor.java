package edu.stanford.bmir.protege.web.shared.crud;

import edu.stanford.bmir.protege.web.client.irigen.obo.OBOIdSuffixSettingsEditor;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/08/2013
 */
@EntityCrudKitPlugin
public class OBOIdSuffixDescriptor extends EntityCrudKitDescriptor {

    private static final OBOIdSuffixDescriptor INSTANCE = new OBOIdSuffixDescriptor();


    private OBOIdSuffixDescriptor() {
        super(EntityCrudKitId.get("OBO"), "Auto-generated  OBO Style Id");
    }

    public static OBOIdSuffixDescriptor get() {
        return INSTANCE;
    }

    @Override
    public EntityCrudKitSuffixSettingsEditor<?> getSuffixSettingsEditor() {
        return new OBOIdSuffixSettingsEditor();
    }

    @Override
    public EntityCrudKitPrefixSettings getDefaultPrefixSettings() {
        return new EntityCrudKitPrefixSettings("http://purl.obolibrary.org/obo/");
    }

    @Override
    public EntityCrudKitSuffixSettings getDefaultSuffixSettings() {
        return new OBOIdSuffixSettings();
    }
}
