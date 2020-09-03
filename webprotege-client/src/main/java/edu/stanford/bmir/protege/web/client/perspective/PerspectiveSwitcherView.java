package edu.stanford.bmir.protege.web.client.perspective;


import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveDescriptor;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;

import java.util.List;
import java.util.Set;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 23/06/2014
 */
public interface PerspectiveSwitcherView extends IsWidget {

    void setResettablePerspectives(Set<PerspectiveId> resettablePerspectives);

    /**
     * Handles notifications for when a perspective link has been activated.
     */
    interface PerspectiveActivatedHandler {
        void handlePerspectiveActivated(PerspectiveId perspectiveId);
    }

    /**
     * Handles request to add a new perspective link.
     */
    interface AddBlankPerspectiveHandler {
        void handleAddBlankPerspective();
    }

    interface AddToFavoritePerspectivesHandler {
        void handleAddToFavorites(PerspectiveDescriptor perspectiveDescriptor);
    }

    interface RemoveFromFavoritePerspectivesHandler {
        void handleRemoveFromFavorites(PerspectiveId perspectiveId);
    }

    interface ResetPerspectiveToDefaultStateHandler {
        void handleResetPerspectiveToDefaultState(PerspectiveDescriptor perspectiveDescriptor);
    }

    interface AddViewHandler {
        void handleAddViewToPerspective(PerspectiveId perspectiveId);
    }

    interface ManagerPerspectivesHandler {
        void handleManagePerspectives();
    }

    /**
     * Sets the selected perspective link.  This link will be visually distinguished from the other perspective
     * links in the view.
     * @param perspectiveId The {@link PerspectiveId}.  Not {@code null}.
     * @throws NullPointerException if {@code perspectiveId} is {@code null}.
     */
    void setHighlightedPerspective(PerspectiveId perspectiveId);

    /**
     * Sets the perspective links that are displayed by this switcher.
     * @param perspectives The list of perspective links that are displayed.  Not {@code null}.
     */
    void setFavourites(List<PerspectiveDescriptor> perspectives);

    void addFavorite(PerspectiveDescriptor perspectiveDescriptor);

    void removeFavorite(PerspectiveDescriptor perspectiveDescriptor);


    /**
     * Sets the handler that will be called when a perspective link is activated.
     * @param handler The handler.  Not {@code null}.
     */
    void setPerspectiveActivatedHandler(PerspectiveActivatedHandler handler);

    /**
     * Sets the handler that will be called when a request to add a new perspective link is made by the user.
     * @param handler The handler.  Not {@code null}.
     */
    void setAddBlankPerspectiveHandler(AddBlankPerspectiveHandler handler);

    void setAddToFavoritePerspectivesHandler(AddToFavoritePerspectivesHandler handler);

    /**
     * Sets the handler that will be called when a request to remove a perspective link is made by the user.
     * @param handler  The handler.  Not {@code null}.
     */
    void setRemoveFromFavoritePerspectivesHandler(RemoveFromFavoritePerspectivesHandler handler);

    void setResetPerspectiveToDefaultStateHandler(ResetPerspectiveToDefaultStateHandler handler);

    void setManagePerspectivesHandler(ManagerPerspectivesHandler handler);

    void setAddViewHandler(AddViewHandler handler);


    void setAvailablePerspectives(List<PerspectiveDescriptor> perspectives);

    void setAddPerspectiveAllowed(boolean addPerspectiveAllowed);

    void setClosePerspectiveAllowed(boolean closePerspectiveAllowed);

    void setAddViewAllowed(boolean addViewAllowed);

    void setManagePerspectivesAllowed(boolean allowed);
}
