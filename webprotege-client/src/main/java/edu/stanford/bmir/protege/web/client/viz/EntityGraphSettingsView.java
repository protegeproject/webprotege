package edu.stanford.bmir.protege.web.client.viz;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-08
 */
public interface EntityGraphSettingsView extends IsWidget {

    @Nonnull
    AcceptsOneWidget getFilterListContainer();

    void setApplySettingsHandler(Runnable runnable);

    void setCancelSettingsHandler(Runnable runnable);

    void setApplySettingsAsProjectDefaultHandler(Runnable runnable);

    void setApplySettingsAsProjectDefaultVisible(boolean b);

    double getRankSpacing();

    void setRankSpacing(double value);
}
