package edu.stanford.bmir.protege.web.client.ui.library.common;

import com.google.gwt.user.client.ui.Widget;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/01/2012
 * <p>
 *     A common interface for objects that expose data in the UI.
 * </p>
 */
public interface WebProtegeDisplay {

    /**
     * Gets the widget that underpins this display.
     * @return The widget underpinning this display.
     */
    Widget getDisplayWidget();

}
