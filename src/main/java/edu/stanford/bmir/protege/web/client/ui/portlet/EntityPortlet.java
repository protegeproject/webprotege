package edu.stanford.bmir.protege.web.client.ui.portlet;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.ui.selection.Selectable;
import edu.stanford.bmir.protege.web.client.ui.selection.SelectionEvent;
import edu.stanford.bmir.protege.web.client.ui.selection.SelectionListener;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.protege.widgetmap.client.view.HasViewTitle;
import edu.stanford.protege.widgetmap.client.view.HasViewTitleChangedHandlers;

import java.util.Collection;

public interface EntityPortlet extends IsWidget, HasDispose, HasViewTitleChangedHandlers, HasViewTitle {

    void setHeight(int height);

    int getHeight();

    void addPortletAction(PortletAction portletAction);

    AcceptsOneWidget getContentHolder();

    void setToolbarVisible(boolean visible);
}
