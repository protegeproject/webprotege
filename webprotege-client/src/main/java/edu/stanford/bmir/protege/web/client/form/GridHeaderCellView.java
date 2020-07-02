package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HasVisibility;
import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-27
 */
public interface GridHeaderCellView extends IsWidget, HasVisibility {

    void setLabel(@Nonnull String label);

    void clearSortOrder();

    void setSortAscending();

    void setSortDescending();

    void setClickHandler(ClickHandler clickHandler);
}
