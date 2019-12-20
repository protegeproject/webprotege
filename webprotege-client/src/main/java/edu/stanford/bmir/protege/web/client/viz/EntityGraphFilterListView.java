package edu.stanford.bmir.protege.web.client.viz;

import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-11
 */
public interface EntityGraphFilterListView extends IsWidget {

    void addItem(@Nonnull EntityGraphFilterListItemView itemView);

    void clear();

    void removeItem(@Nonnull EntityGraphFilterListItemView itemView);

    void setAddItemHandler(Runnable handler);
}
