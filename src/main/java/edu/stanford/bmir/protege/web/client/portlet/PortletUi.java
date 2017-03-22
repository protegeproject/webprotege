package edu.stanford.bmir.protege.web.client.portlet;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.app.HasForbiddenView;
import edu.stanford.bmir.protege.web.client.filter.FilterView;
import edu.stanford.bmir.protege.web.client.progress.HasBusy;
import edu.stanford.protege.widgetmap.client.view.HasViewTitle;
import edu.stanford.protege.widgetmap.client.view.HasViewTitleChangedHandlers;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10/02/16
 */
public interface PortletUi extends IsWidget, AcceptsOneWidget, HasPortletActions, HasViewTitleChangedHandlers, HasForbiddenView, HasViewTitle, HasBusy {

    void setToolbarVisible(boolean visible);

    void setMenuButtonVisible(boolean visible);

    void addMenuAction(PortletAction action);

    void setFilterView(FilterView filterView);

    void setFilterApplied(boolean filterApplied);

    void addPortletAction(PortletAction action);

    void setViewTitle(String title);

    void setNothingSelectedVisible(boolean nothingSelectedVisible);
}
