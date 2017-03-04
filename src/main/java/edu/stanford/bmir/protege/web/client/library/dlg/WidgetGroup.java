package edu.stanford.bmir.protege.web.client.library.dlg;

import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/10/2012
 */
public class WidgetGroup<W extends Widget> {


    private List<W> group = new ArrayList<W>();

    /**
     * Adds a widget to this group
     * @param widget The widget to be added. Not <code>null</code>.
     * @throws NullPointerException if widget is <code>null</code>.
     */
    public void add(W widget) {
        if(widget == null) {
            throw new NullPointerException("widget must not be null");
        }
        group.add(widget);
    }

    /**
     * Removes a widget from this group.
     * @param widget The widget to be removed. Not <code>null</code>.
     */
    public void remove(W widget) {
        if(widget == null) {
            throw new NullPointerException("widget must not be null");
        }
        group.remove(widget);
    }

    /**
     * Removes all of the widgets from this group.
     */
    public void clearGroup() {
        group.clear();
    }


    /**
     * Applies a visibility setting to all widgets in the group.
     * @param b <code>true</code> if all widgets should be made visible, otherwise, <code>false</code> to make all
     * widgets in the group invisible.
     */
    public void setVisible(boolean b) {
        for(W widget : getWidgets()) {
            widget.setVisible(b);
        }
    }
    
    public List<W> getWidgets() {
        return new ArrayList<W>(group);
    }

}
