package edu.stanford.bmir.protege.web.client.viz;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-08
 */
public interface VizSettingsView extends IsWidget {

    @Nonnull
    AcceptsOneWidget getExclusionCriteriaContainer();

    @Nonnull
    AcceptsOneWidget getIncludeCriteriaContainer();

    void setApplySettingsHandler(Runnable runnable);

    void setCancelSettingsHandler(Runnable runnable);
}
