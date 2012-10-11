package edu.stanford.bmir.protege.web.client.ui.library.dlg;

import com.google.gwt.user.client.ui.FocusWidget;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 10/10/2012
 * <p>
 *     An object for managing multiple FocusWidgets.  This makes it possible to enable or disable all of the widgets
 *     in a group for example.
 * </p>
 */
public class FocusWidgetGroup {

    private List<FocusWidget> group = new ArrayList<FocusWidget>();

    /**
     * Adds a widget to this group
     * @param focusWidget The widget to be added. Not <code>null</code>.
     */
    public void add(FocusWidget focusWidget) {
        if(focusWidget == null) {
            throw new NullPointerException("focusWidget must not be null");
        }
        group.add(focusWidget);
    }

    /**
     * Removes a widget from this group.
     * @param focusWidget The widget to be removed. Not <code>null</code>.
     */
    public void remove(FocusWidget focusWidget) {
        if(focusWidget == null) {
            throw new NullPointerException("focusWidget must not be null");
        }
        group.remove(focusWidget);
    }

    /**
     * Removes all of the widgets from this group.
     */
    public void clear() {
        group.clear();
    }
    

    public void setWidgetsEnabled(boolean enabled) {
        for(FocusWidget focusWidget : group) {
            focusWidget.setEnabled(enabled);
        }
    }
}
