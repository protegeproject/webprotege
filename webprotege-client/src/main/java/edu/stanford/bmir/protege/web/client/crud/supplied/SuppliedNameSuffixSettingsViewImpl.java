package edu.stanford.bmir.protege.web.client.crud.supplied;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ListBox;
import edu.stanford.bmir.protege.web.shared.crud.supplied.WhiteSpaceTreatment;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-06
 */
public class SuppliedNameSuffixSettingsViewImpl extends Composite implements SuppliedNameSuffixSettingsView {

    interface SuppliedNameSuffixSettingsViewImplUiBinder extends UiBinder<HTMLPanel, SuppliedNameSuffixSettingsViewImpl> {

    }

    private static SuppliedNameSuffixSettingsViewImplUiBinder ourUiBinder = GWT.create(
            SuppliedNameSuffixSettingsViewImplUiBinder.class);

    @UiField
    ListBox whiteSpaceTreatmentListBox;

    @Inject
    public SuppliedNameSuffixSettingsViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        for(WhiteSpaceTreatment whiteSpaceTreatment : WhiteSpaceTreatment.values()) {
            whiteSpaceTreatmentListBox.addItem(whiteSpaceTreatment.getDisplayName());
        }
    }

    @Override
    public void clearView() {
        whiteSpaceTreatmentListBox.setSelectedIndex(0);
    }

    @Override
    public void setWhiteSpaceTreatment(@Nonnull WhiteSpaceTreatment whiteSpaceTreatment) {
        whiteSpaceTreatmentListBox.setSelectedIndex(whiteSpaceTreatment.ordinal());
    }

    @Nonnull
    @Override
    public WhiteSpaceTreatment getWhiteSpaceTreatment() {
        int selIndex = whiteSpaceTreatmentListBox.getSelectedIndex();
        if(selIndex == -1) {
            return WhiteSpaceTreatment.values()[0];
        }
        return WhiteSpaceTreatment.values()[selIndex];
    }
}
