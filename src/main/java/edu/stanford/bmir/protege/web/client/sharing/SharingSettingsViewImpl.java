package edu.stanford.bmir.protege.web.client.sharing;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import javax.inject.Inject;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.ui.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.ui.editor.ValueEditorFactory;
import edu.stanford.bmir.protege.web.client.ui.editor.ValueListEditorImpl;
import edu.stanford.bmir.protege.web.shared.sharing.SharingPermission;
import edu.stanford.bmir.protege.web.shared.sharing.SharingSetting;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

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
    Button cancelButton;

    @UiField
    Button applyButton;

    @UiField
    CheckBox linkSharingEnabledCheckBox;

    @UiField
    ListBox linkSharingPermissionDropDown;

    @UiField(provided = true)
    ValueListEditorImpl<SharingSetting> sharingSettingsListEditor;

    @UiField
    HTMLPanel linkSharingView;

    private ApplyChangesHandler applyChangesHandler = new ApplyChangesHandler() {
        @Override
        public void handleApplyChanges() {

        }
    };

    private CancelHandler cancelHandler = new CancelHandler() {
        @Override
        public void handleCancel() {

        }
    };

    @Inject
    public SharingSettingsViewImpl(final DispatchServiceManager dispatchServiceManager) {
        sharingSettingsListEditor = new ValueListEditorImpl<>(new ValueEditorFactory<SharingSetting>() {
            @Override
            public ValueEditor<SharingSetting> createEditor() {
                return new SharingSettingEditorImpl(dispatchServiceManager);
            }
        });
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

    @UiHandler("applyButton")
    protected void handleApply(ClickEvent e) {
        applyChangesHandler.handleApplyChanges();
    }


    @UiHandler("cancelButton")
    protected void handleCancel(ClickEvent e) {
        cancelHandler.handleCancel();
    }



    @Override
    public void setLinkSharingPermission(Optional<SharingPermission> sharingPermission) {
        linkSharingEnabledCheckBox.setValue(sharingPermission.isPresent());
        updateLinkSharingPanel();
        if (sharingPermission.isPresent()) {
            linkSharingPermissionDropDown.setSelectedIndex(sharingPermission.get().ordinal());
        }
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
        Optional<List<SharingSetting>> value = Optional.ofNullable(sharingSettingsListEditor.getValue().orNull());
        if(value.isPresent()) {
            return value.get();
        }
        else {
            return Collections.emptyList();
        }
    }

    @Override
    public void setApplyChangesHandler(ApplyChangesHandler handler) {
        applyChangesHandler = checkNotNull(handler);
    }

    @Override
    public void setCancelHandler(CancelHandler handler) {
        cancelHandler = checkNotNull(handler);
    }
}