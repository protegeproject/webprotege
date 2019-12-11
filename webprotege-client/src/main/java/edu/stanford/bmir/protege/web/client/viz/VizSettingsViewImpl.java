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
public class VizSettingsViewImpl extends Composite implements VizSettingsView {

    private Runnable applyHandler = () -> {};

    private Runnable cancelHandler = () -> {};

    interface VizSettingsViewImplUiBinder extends UiBinder<HTMLPanel, VizSettingsViewImpl> {

    }

    private static VizSettingsViewImplUiBinder ourUiBinder = GWT.create(VizSettingsViewImplUiBinder.class);

    @UiField
    SimplePanel includeCriteriaContainer;

    @UiField
    SimplePanel excludeCriteriaContainer;

    @UiField
    Button applyButton;

    @UiField
    Button cancelButton;

    @UiField
    ListBox ranksepListBox;

    @Inject
    public VizSettingsViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        ranksepListBox.setSelectedIndex(1);
        applyButton.addClickHandler(event -> applyHandler.run());
        cancelButton.addClickHandler(event -> cancelHandler.run());
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getExclusionCriteriaContainer() {
        return excludeCriteriaContainer;
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getIncludeCriteriaContainer() {
        return includeCriteriaContainer;
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
}
