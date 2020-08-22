package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-18
 */
public interface ObjectListViewHolder extends IsWidget, AcceptsOneWidget, HasRequestFocus {

    void setMoveDownHandler(@Nonnull Runnable handler);

    void setMoveUpHandler(@Nonnull Runnable handler);

    void setRemoveHandler(@Nonnull Runnable handler);

    void toggleExpansion();

    void setCollapsed();

    void setExpanded();

    boolean isExpanded();

    void setNumber(int number);

    void setHeaderLabel(@Nonnull String headerLabel);

    void scrollIntoView();

    void setFirst();

    void setMiddle();

    void setLast();

    void setPositionOrdinal(int i);
}
