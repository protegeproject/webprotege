package edu.stanford.bmir.protege.web.client.ui.library.common;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/01/2012
 * <p>
 *     A marker interface for objects whose state can be refreshed.  This particularly applies to UI objects such as
 *     tables etc.
 * </p>
 */
public interface Refreshable {

    /**
     * Refreshes this refreshable.
     */
    void refresh();
}
