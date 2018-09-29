package edu.stanford.bmir.protege.web.client.hierarchy;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.UIObject;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Sep 2018
 */
public interface HierarchyFieldView extends IsWidget, HasRequestFocus {


    interface SyncClassWithLastSelectedClassHandler {
        void handleSyncWithLastSelectedClass();
    }

    interface MoveToParentHandler {
        void handleMoveToParent(UIObject target);
    }

    interface MoveToChildHandler {
        void handleMoveToChild(UIObject target);
    }

    interface MoveToSiblingHandler {
        void handleMoveToSibling(UIObject target);
    }

    interface EntityChangedHandler {
        void handleEntityChanged();
    }

    interface ShowPopupHierarchyHandler {
        void handleShowPopupHierarchy(UIObject target);
    }

    void setSyncClassWithLastSelectedClassHandler(@Nonnull SyncClassWithLastSelectedClassHandler handler);

    void setMoveToParentHandler(@Nonnull MoveToParentHandler handler);

    void setMoveToChildHandler(@Nonnull MoveToChildHandler handler);

    void setMoveToSiblingHandler(@Nonnull MoveToSiblingHandler handler);

    void setEntityChangedHandler(@Nonnull EntityChangedHandler handler);

    void setShowPopupHierarchyHandler(@Nonnull ShowPopupHierarchyHandler handler);

    @Nonnull
    Optional<OWLEntityData> getEntity();

    void setEntity(@Nonnull OWLEntityData entity);

    void clearEntity();

    void setSyncClassWithLastSelectedClassEnabled(boolean enabled);

    void setMoveToParentButtonEnabled(boolean enabled);

    void setMoveToChildButtonEnabled(boolean enabled);

    void setMoveToSiblingButtonEnabled(boolean enabled);

    void setEntityType(@Nonnull PrimitiveType entityType);

    void setSyncWithCurrentSelectionVisible(boolean visible);
}
