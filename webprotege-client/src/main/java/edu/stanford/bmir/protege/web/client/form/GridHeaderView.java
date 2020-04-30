package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.form.GridHeaderPresenter.GridColumnVisibilityChangedHandler;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-27
 */
public interface GridHeaderView extends IsWidget {

    void addColumnHeader(@Nonnull IsWidget headerWidget, double weight);

    void clear();

    void addColumnToFilterList(@Nonnull String columnName,
                               @Nonnull GridColumnId columnId);

    ImmutableList<GridColumnId> getVisibleColumns();

    void setGridColumnVisibilityChangedHandler(@Nonnull GridColumnVisibilityChangedHandler handler);

    void setColumnVisible(int columnIndex, boolean visible);
}
