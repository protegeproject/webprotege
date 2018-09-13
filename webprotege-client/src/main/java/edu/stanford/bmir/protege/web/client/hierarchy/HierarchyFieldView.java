package edu.stanford.bmir.protege.web.client.hierarchy;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Sep 2018
 */
public interface HierarchyFieldView extends IsWidget {

    interface MoveToParentHandler {
        void handleMoveToParent();
    }

    interface MoveToChildHandler {
        void handleMoveToChild();
    }

    interface MoveToSiblingHanler {
        void handleMoveToSibling();
    }

    interface EntityChangedHandler {
        void handleEntityChanged();
    }

    void setMoveToParentHandler(@Nonnull MoveToParentHandler handler);

    void setMoveToChildHandler(@Nonnull MoveToChildHandler handler);

    void setMoveToSiblingHandler(@Nonnull MoveToSiblingHanler handler);

    void setEntityChangedHandler(@Nonnull EntityChangedHandler handler);

    @Nonnull
    Optional<OWLEntityData> getEntity();

    void setEntity(@Nonnull OWLEntityData entity);

    void clearEntity();

    void setMoveToParentButtonEnabled(boolean enabled);

    void setMoveToChildButtonEnabled(boolean enabled);

    void setMoveToSiblingButtonEnabled(boolean enabled);
}
