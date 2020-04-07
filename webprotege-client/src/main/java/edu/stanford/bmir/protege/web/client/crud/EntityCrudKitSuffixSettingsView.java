package edu.stanford.bmir.protege.web.client.crud;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-06
 */
public interface EntityCrudKitSuffixSettingsView extends IsWidget {

    interface SelectedSuffixSettingsChangedHandler {
        void handleSelectedSuffixSettingsChanged();
    }

    void setSelectedSuffixSettingsChangedHandler(@Nonnull SelectedSuffixSettingsChangedHandler handler);

    void setAvailableSuffixSettingNames(ImmutableList<String> suffixSettingNames);

    void setSelectedSuffixSettingName(@Nonnull String suffixSettingName);

    @Nonnull
    String getSelectedSuffixSettingName();

    @Nonnull
    AcceptsOneWidget getSettingsViewContainer();
}
