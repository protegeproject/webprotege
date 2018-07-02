package edu.stanford.bmir.protege.web.client.settings;

import com.google.gwt.user.client.ui.AcceptsOneWidget;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2 Jul 2018
 */
public interface SettingsSectionPresenter {

    @Nonnull
    String getSectionName();

    void start(@Nonnull AcceptsOneWidget sectionContainer);

    void handleApplySettings(@Nonnull ApplySettingsCallback callback);
}
