package edu.stanford.bmir.protege.web.server.crud.obo;

import edu.stanford.bmir.protege.web.server.crud.EntityCrudKitHandler;
import edu.stanford.bmir.protege.web.server.crud.EntityCrudKitPlugin;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKit;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitPrefixSettings;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSettings;
import edu.stanford.bmir.protege.web.shared.crud.oboid.OBOIdSuffixKit;
import edu.stanford.bmir.protege.web.shared.crud.oboid.OBOIdSuffixSettings;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/19/13
 */
public class OBOIdSuffixEntityCrudKitPlugin implements EntityCrudKitPlugin<OBOIdSuffixEntityCrudKitHandler, OBOIdSuffixSettings> {
    @Override
    public EntityCrudKit<OBOIdSuffixSettings> getEntityCrudKit() {
        return OBOIdSuffixKit.get();
    }

    @Override
    public EntityCrudKitHandler<OBOIdSuffixSettings> getEntityCrudKitHandler() {
        return new OBOIdSuffixEntityCrudKitHandler(new EntityCrudKitPrefixSettings(), new OBOIdSuffixSettings());
    }

    @Override
    public EntityCrudKitHandler<OBOIdSuffixSettings> getEntityCrudKitHandler(EntityCrudKitSettings<OBOIdSuffixSettings> settings) {
        return new OBOIdSuffixEntityCrudKitHandler(settings.getPrefixSettings(), settings.getSuffixSettings());
    }

    @Override
    public OBOIdSuffixSettings getDefaultSettings() {
        return new OBOIdSuffixSettings();
    }
}
