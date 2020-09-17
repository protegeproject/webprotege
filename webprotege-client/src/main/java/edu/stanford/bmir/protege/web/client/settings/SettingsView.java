package edu.stanford.bmir.protege.web.client.settings;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.progress.HasBusy;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2 Jul 2018
 */
public interface SettingsView extends IsWidget, HasBusy {

    void clearView();

    void addSectionViewContainer(@Nonnull SettingsSectionViewContainer view);

    void setApplyButtonText(@Nonnull String text);

    void setCancelButtonText(@Nonnull String text);

    void setProjectTitle(@Nonnull String projectTitle);

    void setApplyButtonVisible(boolean visible);

    void setCancelButtonVisible(boolean visible);

    void setApplySettingsHandler(@Nonnull ApplySettingsHandler applyButtonHandler);

    void setCancelSettingsHandler(@Nonnull CancelSettingsHandler cancelButtonHandler);

    void showErrorMessage(@Nonnull String msgTitle,
                          @Nonnull String msgBody);

    void setApplySettingsStarted();

    void setApplySettingsFinished();

    void setSettingsTitle(@Nonnull String settingsTitle);
}
