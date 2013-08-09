package edu.stanford.bmir.protege.web.server.crud;

import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitDescriptor;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitManager;
import edu.stanford.bmir.protege.web.shared.crud.HasKitId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/08/2013
 */
public interface EntityCrudKitFactory extends HasKitId {

    EntityCrudKitDescriptor getDescriptor();

    EntityCrudKit createEntityCrudKit();

    EntityCrudKit createEntityCrudKit(EntityCrudKitDescriptor descriptor);

    EntityCrudKitManager createSettingsManager();
}
