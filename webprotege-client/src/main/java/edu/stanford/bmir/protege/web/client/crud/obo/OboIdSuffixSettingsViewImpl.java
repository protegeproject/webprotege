package edu.stanford.bmir.protege.web.client.crud.obo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import edu.stanford.bmir.protege.web.client.editor.ValueListFlexEditorImpl;
import edu.stanford.bmir.protege.web.shared.crud.oboid.UserIdRange;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Collections;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-06
 */
public class OboIdSuffixSettingsViewImpl extends Composite implements OboIdSuffixSettingsView {

    interface OboIdSuffixSettingsViewImplUiBinder extends UiBinder<HTMLPanel, OboIdSuffixSettingsViewImpl> {

    }

    private static OboIdSuffixSettingsViewImplUiBinder ourUiBinder = GWT.create(OboIdSuffixSettingsViewImplUiBinder.class);

    @UiField(provided = true)
    ValueListFlexEditorImpl<UserIdRange> userRangeTable;

    @UiField
    TextBox totalDigitsEditor;

    @Inject
    public OboIdSuffixSettingsViewImpl(Provider<UserIdRangeEditor> userIdRangeEditorProvider) {
        userRangeTable = new ValueListFlexEditorImpl<>(userIdRangeEditorProvider::get);
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void clearView() {
        totalDigitsEditor.setText("7");
    }

    @Override
    public void setTotalDigits(String totalDigits) {
        totalDigitsEditor.setText(totalDigits);
    }

    @Nonnull
    @Override
    public String getTotalDigits() {
        return totalDigitsEditor.getText().trim();
    }

    @Override
    public void setUserIdRanges(@Nonnull List<UserIdRange> ranges) {
        userRangeTable.setValue(ranges);
    }

    @Nonnull
    @Override
    public List<UserIdRange> getUserIdRanges() {
        return userRangeTable.getValue().orElse(Collections.emptyList());
    }
}
