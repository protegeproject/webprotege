package edu.stanford.bmir.protege.web.client.topbar;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16/02/16
 */
public interface TopBarView extends IsWidget {

    AcceptsOneWidget getGoToHomeWidgetContainer();

    void addToLeft(IsWidget widget);

    void addToCenter(IsWidget widget);

    void addToRight(IsWidget widget);

    AcceptsOneWidget getLoggedInUserWidgetContainer();

    AcceptsOneWidget getHelpWidgetContainer();

}
