package edu.stanford.bmir.protege.web.client.ui.library.dlg;

import com.google.gwt.user.client.ui.FocusWidget;

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
public class FocusWidgetGroup extends WidgetGroup<FocusWidget> {

    public void setWidgetsEnabled(boolean enabled) {
        for(FocusWidget focusWidget : getWidgets()) {
            focusWidget.setEnabled(enabled);
        }
    }
}
