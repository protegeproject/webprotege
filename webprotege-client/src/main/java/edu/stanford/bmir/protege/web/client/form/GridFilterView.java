package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-06-17
 */
public interface GridFilterView extends IsWidget {

    interface ClearFiltersHandler {
        void handleClearFilters();
    }

    @Nonnull
    AcceptsOneWidget addFilter(@Nonnull String columnName);

    void clear();

    void setClearFiltersHandler(@Nonnull ClearFiltersHandler handler);
}
