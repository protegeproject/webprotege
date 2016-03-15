package edu.stanford.bmir.protege.web.client.sharing;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SuggestBox;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.itemlist.PersonIdItemListSuggestionOracle;
import edu.stanford.bmir.protege.web.client.user.UserIdSuggestOracle;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.sharing.PersonId;
import edu.stanford.bmir.protege.web.shared.sharing.SharingPermission;
import edu.stanford.bmir.protege.web.shared.sharing.SharingSetting;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13/03/16
 */
public class SharingSettingEditorImpl extends Composite implements SharingSettingEditor {

    interface SharingSettingEditorImplUiBinder extends UiBinder<HTMLPanel, SharingSettingEditorImpl> {

    }

    private static SharingSettingEditorImplUiBinder ourUiBinder = GWT.create(SharingSettingEditorImplUiBinder.class);


    @UiField(provided = true)
    SuggestBox personIdField;

    @UiField
    ListBox permissionField;


    private boolean dirty = false;

    public SharingSettingEditorImpl(DispatchServiceManager dispatchServiceManager) {
        personIdField = new SuggestBox(new UserIdSuggestOracle(dispatchServiceManager));
        initWidget(ourUiBinder.createAndBindUi(this));
        for(SharingPermission sharingPermission : SharingPermission.values()) {
            permissionField.addItem(sharingPermission.name());
        }
    }


    @UiHandler("personIdField")
    protected void handlePersonIdChanged(ValueChangeEvent<String> e) {
        dirty = true;
        ValueChangeEvent.fire(this, getValue());
    }

    @Override
    public void setValue(SharingSetting object) {
        permissionField.setSelectedIndex(object.getSharingPermission().ordinal());
        personIdField.setValue(object.getPersonId().getId());
        dirty = false;
    }

    @Override
    public void clearValue() {
        permissionField.setSelectedIndex(0);
        personIdField.setText("");
        dirty = false;
    }

    @Override
    public Optional<SharingSetting> getValue() {
        return Optional.of(
                new SharingSetting(new PersonId(personIdField.getText().trim()), SharingPermission.values()[permissionField.getSelectedIndex()])
        );
    }

    @Override
    public boolean isDirty() {
        return dirty;
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return addHandler(handler, DirtyChangedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<SharingSetting>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public boolean isWellFormed() {
        return !personIdField.getText().trim().isEmpty();
    }
}