package edu.stanford.bmir.protege.web.server.crud.supplied;

import edu.stanford.bmir.protege.web.server.crud.ChangeSetEntityCrudSession;
import edu.stanford.bmir.protege.web.server.crud.EntityCrudKitHandler;
import edu.stanford.bmir.protege.web.server.crud.EntityCrudKitPlugin;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKit;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSettings;
import edu.stanford.bmir.protege.web.shared.crud.supplied.SuppliedNameSuffixKit;
import edu.stanford.bmir.protege.web.shared.crud.supplied.SuppliedNameSuffixSettings;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/19/13
 */
public class SuppliedNameSuffixEntityCrudKitPlugin implements EntityCrudKitPlugin<SuppliedNameSuffixEntityCrudKitHandler, SuppliedNameSuffixSettings, ChangeSetEntityCrudSession> {

    @Override
    public EntityCrudKit<SuppliedNameSuffixSettings> getEntityCrudKit() {
        return SuppliedNameSuffixKit.get();
    }

    @Override
    public EntityCrudKitHandler<SuppliedNameSuffixSettings, ChangeSetEntityCrudSession> getEntityCrudKitHandler() {
        return new SuppliedNameSuffixEntityCrudKitHandler();
    }

    @Override
    public EntityCrudKitHandler<SuppliedNameSuffixSettings, ChangeSetEntityCrudSession> getEntityCrudKitHandler(EntityCrudKitSettings<SuppliedNameSuffixSettings> settings) {
        return new SuppliedNameSuffixEntityCrudKitHandler(settings.getPrefixSettings(), settings.getSuffixSettings());
    }

    @Override
    public SuppliedNameSuffixSettings getDefaultSettings() {
        return new SuppliedNameSuffixSettings();
    }
}
