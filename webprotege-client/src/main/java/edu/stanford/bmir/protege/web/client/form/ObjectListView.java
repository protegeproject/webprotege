package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-17
 */
public interface ObjectListView extends IsWidget {

    void clear();

    void addView(@Nonnull ObjectListViewHolder view);

    void performDeleteElementConfirmation(String objectId,
                                          @Nonnull Runnable deleteRunnable);

    void removeView(@Nonnull ObjectListViewHolder viewHolder);

    void moveUp(@Nonnull ObjectListViewHolder viewHolder);

    void moveDown(@Nonnull ObjectListViewHolder viewHolder);

    void setAddObjectText(@Nonnull String addObjectText);

    void setAddObjectHandler(Runnable handler);
}
