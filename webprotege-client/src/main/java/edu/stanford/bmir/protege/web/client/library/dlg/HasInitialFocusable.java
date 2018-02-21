package edu.stanford.bmir.protege.web.client.library.dlg;

import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/10/2012
 */
public interface HasInitialFocusable {

    Optional<HasRequestFocus> getInitialFocusable();
}
