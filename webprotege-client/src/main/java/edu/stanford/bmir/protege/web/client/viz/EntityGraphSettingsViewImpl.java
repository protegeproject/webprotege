package edu.stanford.bmir.protege.web.client.viz;

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
 * 2019-12-08
 */
public class EntityGraphSettingsViewImpl extends Composite implements EntityGraphSettingsView {

    private Runnable applyHandler = () -> {};

    private Runnable cancelHandler = () -> {};

    private Runnable applyProjectDefaultHandler = () -> {};

    interface VizSettingsViewImplUiBinder extends UiBinder<HTMLPanel, EntityGraphSettingsViewImpl> {

    }

    private static VizSettingsViewImplUiBinder ourUiBinder = GWT.create(VizSettingsViewImplUiBinder.class);

    @UiField
    Button applyButton;

    @UiField
    Button cancelButton;

    @UiField
    ListBox ranksepListBox;

    @UiField
    SimplePanel filterListContainer;

    @UiField
    Button applyProjectDefaultButton;

    @Inject
    public EntityGraphSettingsViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        ranksepListBox.setSelectedIndex(1);
        applyButton.addClickHandler(event -> applyHandler.run());
        cancelButton.addClickHandler(event -> cancelHandler.run());
        applyProjectDefaultButton.addClickHandler(event -> applyProjectDefaultHandler.run());
    }


    @Nonnull
    @Override
    public AcceptsOneWidget getFilterListContainer() {
        return filterListContainer;
    }

    @Override
    public void setApplySettingsHandler(Runnable runnable) {
        this.applyHandler = checkNotNull(runnable);
    }

    @Override
    public void setCancelSettingsHandler(Runnable runnable) {
        this.cancelHandler = checkNotNull(runnable);
    }

    @Override
    public double getRankSpacing() {
        String value = ranksepListBox.getSelectedValue();
        try {
            return 2 * Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 2.0;
        }
    }

    @Override
    public void setRankSpacing(double value) {
        double val = value / 2.0;
        for(int index = 0; index < ranksepListBox.getItemCount(); index++) {
            String valAtIndex = ranksepListBox.getValue(index);
            double doubleValue = Double.parseDouble(valAtIndex);
            if(doubleValue == val) {
                ranksepListBox.setSelectedIndex(index);
                return;
            }
        }
    }

    @Override
    public void setApplySettingsAsProjectDefaultHandler(Runnable runnable) {
        this.applyProjectDefaultHandler = checkNotNull(runnable);
    }

    @Override
    public void setApplySettingsAsProjectDefaultVisible(boolean b) {
        applyProjectDefaultButton.setVisible(b);
    }
}
