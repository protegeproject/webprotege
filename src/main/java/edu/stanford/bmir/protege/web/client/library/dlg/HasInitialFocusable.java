package edu.stanford.bmir.protege.web.client.library.dlg;

import com.google.common.base.Optional;
import com.google.gwt.user.client.ui.Focusable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/10/2012
 */
public interface HasInitialFocusable {

    public Optional<Focusable> getInitialFocusable();
}
