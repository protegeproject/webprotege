package edu.stanford.bmir.protege.web.client.sharing;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.editor.ValueListFlexEditorImpl;
import edu.stanford.bmir.protege.web.shared.sharing.SharingPermission;
import edu.stanford.bmir.protege.web.shared.sharing.SharingSetting;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13/03/16
 */
public class SharingSettingsViewImpl extends Composite implements SharingSettingsView {

    interface SharingSettingsViewImplUiBinder extends UiBinder<HTMLPanel, SharingSettingsViewImpl> {

    }

    private static SharingSettingsViewImplUiBinder ourUiBinder = GWT.create(SharingSettingsViewImplUiBinder.class);

    @UiField
    CheckBox linkSharingEnabledCheckBox;

    @UiField
    ListBox linkSharingPermissionDropDown;

    @UiField(provided = true)
    ValueListFlexEditorImpl<SharingSetting> sharingSettingsListEditor;

    @UiField
    HTMLPanel linkSharingView;

    @Inject
    public SharingSettingsViewImpl(final DispatchServiceManager dispatchServiceManager) {
        sharingSettingsListEditor = new ValueListFlexEditorImpl<>(() -> new SharingSettingEditorImpl(dispatchServiceManager));
        initWidget(ourUiBinder.createAndBindUi(this));
        for(SharingPermission permission : SharingPermission.values()) {
            linkSharingPermissionDropDown.addItem(permission.name());
        }
        updateLinkSharingPanel();
    }

    @UiHandler("linkSharingEnabledCheckBox")
    protected void handleLinkSharingSettingChanged(ValueChangeEvent<Boolean> e) {
        linkSharingPermissionDropDown.setEnabled(linkSharingEnabledCheckBox.getValue());
        updateLinkSharingPanel();
    }

    @Override
    public void setLinkSharingPermission(Optional<SharingPermission> sharingPermission) {
        linkSharingEnabledCheckBox.setValue(sharingPermission.isPresent());
        updateLinkSharingPanel();
        sharingPermission.ifPresent(permission -> linkSharingPermissionDropDown.setSelectedIndex(permission.ordinal()));
    }

    private void updateLinkSharingPanel() {
        if(linkSharingEnabledCheckBox.getValue()) {
            linkSharingPermissionDropDown.setEnabled(true);
            linkSharingView.setVisible(true);
        }
        else {
            linkSharingPermissionDropDown.setEnabled(false);
            linkSharingView.setVisible(false);
        }
    }

    @Override
    public Optional<SharingPermission> getLinkSharingPermission() {
        if(linkSharingEnabledCheckBox.getValue()) {
            SharingPermission permission = SharingPermission.values()[linkSharingPermissionDropDown.getSelectedIndex()];
            return Optional.of(permission);
        }
        else {
            return Optional.empty();
        }
    }

    @Override
    public void setSharingSettings(List<SharingSetting> sharingSettings) {
        sharingSettingsListEditor.setValue(sharingSettings);
    }

    @Override
    public List<SharingSetting> getSharingSettings() {
        Optional<List<SharingSetting>> value = sharingSettingsListEditor.getValue();
        return value.orElseGet(Collections::emptyList);
    }
}