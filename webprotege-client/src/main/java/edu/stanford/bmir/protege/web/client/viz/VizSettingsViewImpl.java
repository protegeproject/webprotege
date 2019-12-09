package edu.stanford.bmir.protege.web.client.viz;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-08
 */
public class VizSettingsViewImpl extends Composite implements VizSettingsView {

    interface VizSettingsViewImplUiBinder extends UiBinder<HTMLPanel, VizSettingsViewImpl> {

    }

    private static VizSettingsViewImplUiBinder ourUiBinder = GWT.create(VizSettingsViewImplUiBinder.class);

    @UiField
    SimplePanel edgeCriteriaContainer;

    @Inject
    public VizSettingsViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getEdgeCriteriaContainer() {
        return edgeCriteriaContainer;
    }
}
