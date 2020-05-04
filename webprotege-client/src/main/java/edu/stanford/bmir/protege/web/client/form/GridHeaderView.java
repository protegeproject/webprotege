package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-27
 */
public interface GridHeaderView extends IsWidget, HasGridColumnFilter {

    @Nonnull
    GridHeaderCellContainer addColumnHeader();

    void clear();
}
