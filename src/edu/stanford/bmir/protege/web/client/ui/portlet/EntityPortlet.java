package edu.stanford.bmir.protege.web.client.ui.portlet;

import java.util.Collection;

import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.ui.selection.Selectable;

/**
 * Interface that should be implemented by all portlets available in user
 * interface of a tab.<br />
 * <br />
 * 
 * It is recommended that all portlets extend the {@link AbstractEntityPortlet}
 * class.
 * 
 * @author Tania Tudorache <tudorache@stanford.edu>
 */
public interface EntityPortlet extends Selectable {

    /**
     * Called when the portlet should display the content of
     * <code>newEntity</code>.
     * 
     * @param newEntity
     *            - the new entity to render
     */
    public void setEntity(EntityData newEntity);

    /**
     * @return The current entity being rendered.
     */
    public EntityData getEntity();

    /**
     * Called to reload the UI with the content of the current entity.
     */
    public void reload();

    /**
     * Called in the constructor of the portlet to initialize any needed GUI.
     */
    public void initialize();

    /**
     * Called when a user logs in.
     * 
     * @param userName
     *            - the name of the user who logged in.
     */
    public void onLogin(String userName);

    /**
     * Called when a user logs out.
     * 
     * @param userName
     *            - the name of the user who logged out.
     */
    public void onLogout(String userName);

    /**
     * Called when the permissions for the project and user of the portlet have
     * changed
     */
    public void onPermissionsChanged(Collection<String> permissions);
}