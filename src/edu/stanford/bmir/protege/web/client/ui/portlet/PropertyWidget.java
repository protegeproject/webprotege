package edu.stanford.bmir.protege.web.client.ui.portlet;

import java.util.Collection;

import com.gwtext.client.widgets.Component;

import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.PropertyEntityData;

/**
 * The interface for widgets used to display the value of a property at a
 * instance (or class, or property).
 *
 * The property widget has its own workflow:
 * On activate, on refresh, and setEntity it will get the values from the server by
 * making a remote call. When it gets the values back, it will call {@link #setValues(Collection)}
 *  where the argument is the collection of values that comes back from the server.
 *
 * @author Tania Tudorache <tudorache@stanford.edu>
 *
 */
public interface PropertyWidget {

    Collection<EntityData> getValues();

    /**
     * Called by the widget itself after retrieving from the server via a
     * remote call the values for the subject and property. This method is responsible
     * for updating the display or UI of the widget with the new values.
     *
     * @param values - the values for the subject and property (usually come from the
     * a remote call to the server that is made automatically by the widget)
     */
    void setValues(Collection<EntityData> values);

    void setSubject(EntityData subject);

    void setProperty(PropertyEntityData property);

    EntityData getSubject();

    PropertyEntityData getProperty();

    Component getComponent();

    /**
     * Makes a call to the server to get the values for this widget.
     * Once the values come back, it will call {@link #setValues(Collection)}.
     */
    void refresh();

}
