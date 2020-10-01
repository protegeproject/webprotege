package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;

import javax.annotation.Nonnull;
import java.util.function.Function;

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

    void setDeleteConfirmationTitle(@Nonnull Function<String, String> deleteConfirmationTitle);

    void setDeleteConfirmationMessage(@Nonnull Function<String, String> deleteConfirmationMessage);
}
