package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableSet;
import com.google.web.bindery.event.shared.HandlerRegistration;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-05-03
 */
public interface HasGridColumnFilter {

    interface FilteredColumnsChangedHandler {
        void handleFilteredColumnsChanged(@Nonnull HasGridColumnFilter source);
    }

    HandlerRegistration addFilteredColumnsChangedHandler(@Nonnull FilteredColumnsChangedHandler handler);

    void addColumnToFilterList(@Nonnull String columnName,
                               @Nonnull GridColumnId columnId);

    /**
     * Get the columns that are checked in the filter list
     * @return The set of checked columns in the filter list
     */
    ImmutableSet<GridColumnId> getFilteredColumns();
}
