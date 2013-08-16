package edu.stanford.bmir.protege.web.server.crud;

import edu.stanford.bmir.protege.web.shared.crud.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/08/2013
 */
public interface EntityCrudKitFactory<K extends EntityCrudKit, S extends EntityCrudKitSuffixSettings> extends HasKitId {

    K createEntityCrudKit();

    K createEntityCrudKit(EntityCrudKitPrefixSettings prefixSettings, S settings);
}
