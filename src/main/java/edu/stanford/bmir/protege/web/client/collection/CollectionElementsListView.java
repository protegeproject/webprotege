package edu.stanford.bmir.protege.web.client.collection;

import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.pagination.HasPages;
import edu.stanford.bmir.protege.web.shared.collection.CollectionElementId;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Jul 2017
 */
public interface CollectionElementsListView extends IsWidget, HasPages {

    void setSelectionChangedHandler(@Nonnull SelectionChangedHandler handler);

    void setSelection(@Nonnull CollectionElementId selection);

    void clearSelection();

    @Nonnull
    Optional<CollectionElementId> getSelection();

    void setElements(@Nonnull List<CollectionElementId> elements);

    interface SelectionChangedHandler {
        void handleSelectionChanged();
    }

    interface  FilterElementsHandler {
        void handleFilterElements();
    }
}
