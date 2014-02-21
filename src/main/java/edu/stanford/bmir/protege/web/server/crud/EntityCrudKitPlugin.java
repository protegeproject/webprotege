package edu.stanford.bmir.protege.web.server.crud;

import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKit;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSettings;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSuffixSettings;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/19/13
 */
public interface EntityCrudKitPlugin<H extends EntityCrudKitHandler<S>, S extends EntityCrudKitSuffixSettings> {

    EntityCrudKit<S> getEntityCrudKit();

    EntityCrudKitHandler<S> getEntityCrudKitHandler();

    EntityCrudKitHandler<S> getEntityCrudKitHandler(EntityCrudKitSettings<S> settings);

    S getDefaultSettings();
}
