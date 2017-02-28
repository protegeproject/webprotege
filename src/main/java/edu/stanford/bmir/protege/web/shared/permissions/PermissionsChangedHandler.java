package edu.stanford.bmir.protege.web.shared.permissions;

import com.google.gwt.event.shared.EventHandler;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionsChangedEvent;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 03/04/2013
 */
public interface PermissionsChangedHandler extends EventHandler {

    void handlePersmissionsChanged(PermissionsChangedEvent event);
}
