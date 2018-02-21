package edu.stanford.bmir.protege.web.client.collection;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RequiresResize;
import edu.stanford.bmir.protege.web.shared.collection.CollectionItem;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 20 Jul 2017
 */
public interface CollectionView extends IsWidget, RequiresResize {

    void setCollectionTitle(@Nonnull String title);

    @Nonnull
    AcceptsOneWidget getFormContainer();

    @Nonnull
    AcceptsOneWidget getListContainer();

    void setItem(@Nonnull CollectionItem id);

    void setAddItemHandler(AddItemHandler handler);

    void setClearItemDataHandler(ClearItemDataHandler handler);

    interface AddItemHandler {
        void handleAddItem();
    }

    interface DeleteItemHandler {
        void handleDeleteItem(CollectionItem elementId);
    }

    interface ClearItemDataHandler {
        void handleClearItemData();
    }

}
