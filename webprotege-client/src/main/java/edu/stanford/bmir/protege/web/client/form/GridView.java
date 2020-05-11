package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.client.pagination.HasPages;
import edu.stanford.bmir.protege.web.client.pagination.HasPagination;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-25
 */
public interface GridView extends IsWidget, HasPagination, HasPages, HasEnabled {

    void setEnabled(boolean enabled);

    interface NewRowHandler {
        void handleAddNewRow();
    }

    @Nonnull
    AcceptsOneWidget getHeaderContainer();

    void requestFocus();

    @Nonnull
    GridRowViewContainer addRow();

    void removeRow(@Nonnull GridRowViewContainer rowContainer);

    void setNewRowHandler(@Nonnull NewRowHandler newRowHandler);

    void clear();

    void hideHeader();

    void setPaginatorVisible(boolean visible);
}
