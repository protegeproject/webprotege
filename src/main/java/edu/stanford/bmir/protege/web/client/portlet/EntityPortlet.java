package edu.stanford.bmir.protege.web.client.portlet;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.protege.widgetmap.client.view.HasViewTitle;
import edu.stanford.protege.widgetmap.client.view.HasViewTitleChangedHandlers;

public interface EntityPortlet extends IsWidget, HasDispose, HasViewTitleChangedHandlers, HasViewTitle, HasPortletActions {

    AcceptsOneWidget getContentHolder();

    void setToolbarVisible(boolean visible);
}
