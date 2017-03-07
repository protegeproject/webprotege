package edu.stanford.bmir.protege.web.client.topbar;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16/02/16
 */
public interface TopBarView extends IsWidget {

    void setProjectTitle(@Nonnull String projectTitle);

    void clearProjectTitle();

    AcceptsOneWidget getGoToHomeWidgetContainer();

    void addToLeft(IsWidget widget);

    void addToCenter(IsWidget widget);

    void addToRight(IsWidget widget);

    AcceptsOneWidget getProjectMenuContainer();

    AcceptsOneWidget getSharingSettingsContainer();

    AcceptsOneWidget getLoggedInUserWidgetContainer();

    AcceptsOneWidget getHelpWidgetContainer();

}
