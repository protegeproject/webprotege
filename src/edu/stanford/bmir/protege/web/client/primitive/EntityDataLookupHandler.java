package edu.stanford.bmir.protege.web.client.primitive;

import com.google.common.base.Optional;
import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/09/2013
 */
public interface EntityDataLookupHandler {

    void lookupEntity(String displayName, EntityDataLookupContext lookupContext, AsyncCallback<Optional<OWLEntityData>> callback);
}
