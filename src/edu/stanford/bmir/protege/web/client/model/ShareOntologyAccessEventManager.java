package edu.stanford.bmir.protege.web.client.model;

import edu.stanford.bmir.protege.web.client.model.event.UpdateShareLinkEvent;
import edu.stanford.bmir.protege.web.client.model.listener.ShareOntologyAccessListener;

import java.util.ArrayList;
import java.util.Collection;

/**
 * This is a singleton class that contains listeners to manage share link
 * visibility depending on whether the user is owner of project or not.
 *
 * @author z.khan
 *
 */
public class ShareOntologyAccessEventManager {

    private static ShareOntologyAccessEventManager shareOntologyAccessEventManager;

    private Collection<ShareOntologyAccessListener> shareLinkVisibilityListeners = new ArrayList<ShareOntologyAccessListener>();

    private ShareOntologyAccessEventManager() {
    }

    public static ShareOntologyAccessEventManager getShareOntologyAccessManager() {
        if (shareOntologyAccessEventManager == null) {
            shareOntologyAccessEventManager = new ShareOntologyAccessEventManager();
        }
        return shareOntologyAccessEventManager;
    }

    /**
     * This listener will only receive events when a different project is
     * selected by user.
     *
     * @param listener
     */
    public void addShareLinkVisibilityListener(ShareOntologyAccessListener listener) {
        shareLinkVisibilityListeners.add(listener);
    }

    /**
     * Updates Share link visibility and currentSelectedProject variable in
     * Toppanel
     *
     * @param showShareLink
     * @param currentSelectedProject
     */
    public void notifyToUpdateShareLink(boolean showShareLink, String currentSelectedProject) {
        for (ShareOntologyAccessListener listener : shareLinkVisibilityListeners) {
            listener.updateShareLink(new UpdateShareLinkEvent(showShareLink, currentSelectedProject));
        }

    }

    /**
     * Updates Share link visibility without updating the currentSelectedProject
     * variable in Toppanel
     *
     * @param showShareLink
     */
    public void notifyToUpdateShareLink(boolean showShareLink) {
        for (ShareOntologyAccessListener listener : shareLinkVisibilityListeners) {
            listener.updateShareLink(showShareLink);
        }

    }

    public void dispose() {
        shareLinkVisibilityListeners.clear();
    }

}
