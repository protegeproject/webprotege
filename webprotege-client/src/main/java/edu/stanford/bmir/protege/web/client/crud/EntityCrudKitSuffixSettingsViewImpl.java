package edu.stanford.bmir.protege.web.client.crud;

import com.google.common.collect.ImmutableList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-06
 */
public class EntityCrudKitSuffixSettingsViewImpl extends Composite implements EntityCrudKitSuffixSettingsView {


    private SelectedSuffixSettingsChangedHandler handler = () -> {};

    interface EntityCrudKitSuffixSettingsViewImplUiBinder extends UiBinder<HTMLPanel, EntityCrudKitSuffixSettingsViewImpl> {

    }

    private static EntityCrudKitSuffixSettingsViewImplUiBinder ourUiBinder = GWT.create(
            EntityCrudKitSuffixSettingsViewImplUiBinder.class);

    @UiField
    SimplePanel settingsContainer;

    @UiField
    ListBox suffixSelectorListBox;

    @Inject
    public EntityCrudKitSuffixSettingsViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        suffixSelectorListBox.addChangeHandler(event -> handler.handleSelectedSuffixSettingsChanged());
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getSettingsViewContainer() {
        return settingsContainer;
    }

    @Override
    public void setSelectedSuffixSettingsChangedHandler(@Nonnull SelectedSuffixSettingsChangedHandler handler) {
        this.handler = checkNotNull(handler);
    }

    @Override
    public void setAvailableSuffixSettingNames(ImmutableList<String> suffixSettingNames) {
        suffixSelectorListBox.clear();
        suffixSettingNames.forEach(name -> suffixSelectorListBox.addItem(name, name));
    }

    @Override
    public void setSelectedSuffixSettingName(@Nonnull String suffixSettingName) {
        for(int i = 0; i < suffixSelectorListBox.getItemCount(); i++) {
            String item = suffixSelectorListBox.getValue(i);
            if(item.equals(suffixSettingName)) {
                suffixSelectorListBox.setSelectedIndex(i);
                return;
            }
        }
    }

    @Nonnull
    @Override
    public String getSelectedSuffixSettingName() {
        return suffixSelectorListBox.getSelectedValue();
    }
}
