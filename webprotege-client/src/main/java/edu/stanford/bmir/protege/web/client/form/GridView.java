package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnDescriptor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-25
 */
public interface GridView extends IsWidget, HasRequestFocus {

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

    void setLimitedRowsDisplayed(int visibleRows, int totalRows);
}
