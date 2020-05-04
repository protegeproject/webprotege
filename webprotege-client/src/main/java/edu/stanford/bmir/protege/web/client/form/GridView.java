package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.client.pagination.HasPagination;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-25
 */
public interface GridView extends IsWidget, HasRequestFocus, HasPagination {

    void setEnabled(boolean enabled);

    interface NewRowHandler {
        GridRowPresenter createRow();
    }

    @Nonnull
    AcceptsOneWidget getHeaderContainer();

    void requestFocus();

    void setRows(@Nonnull List<GridRowPresenter> rowPresenters);

    @Nonnull
    List<GridRowPresenter> getRows();

    void setNewRowHandler(@Nonnull NewRowHandler newRowHandler);

    void clear();

    void hideHeader();

    void setPaginatorVisible(boolean visible);
}
