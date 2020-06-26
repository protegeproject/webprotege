package edu.stanford.bmir.protege.web.client.form;

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
 * 2020-01-11
 */
public class DynamicChoiceListSourceDescriptorViewImpl extends Composite implements DynamicChoiceListSourceDescriptorView {

    @UiField
    protected SimplePanel criteriaContainer;

    interface DynamicChoiceListSourceDescriptorViewImplUiBinder extends UiBinder<HTMLPanel, DynamicChoiceListSourceDescriptorViewImpl> {

    }

    private static DynamicChoiceListSourceDescriptorViewImplUiBinder ourUiBinder = GWT.create(
            DynamicChoiceListSourceDescriptorViewImplUiBinder.class);

    @Inject
    public DynamicChoiceListSourceDescriptorViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getCriteriaContainer() {
        return criteriaContainer;
    }

}
