package edu.stanford.bmir.protege.web.client.list;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasDoubleClickHandlers;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.pagination.HasPagination;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Sep 2018
 */
public interface EntityNodeListPopupView extends IsWidget, HasPagination, HasDoubleClickHandlers {

    interface AcceptSelectionHandler {
        void handleAcceptSelection(@Nonnull OWLEntityData entityData);
    }

    void setAcceptSelectionHandler(@Nonnull AcceptSelectionHandler handler);

    void setListData(@Nonnull List<EntityNode> pageElements);

    @Nonnull
    Optional<EntityNode> getFirstSelectedElement();

    @Nonnull
    List<EntityNode> getSelection();

    void setTitleLabel(@Nonnull String title);
}
