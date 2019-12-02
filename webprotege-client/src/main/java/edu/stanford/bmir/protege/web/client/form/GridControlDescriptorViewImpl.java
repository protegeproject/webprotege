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
 * 2019-11-26
 */
public class GridControlDescriptorViewImpl extends Composite implements GridControlDescriptorView {

    interface GridFieldDescriptorViewImplUiBinder extends UiBinder<HTMLPanel, GridControlDescriptorViewImpl> {

    }

    private static GridFieldDescriptorViewImplUiBinder ourUiBinder = GWT.create(GridFieldDescriptorViewImplUiBinder.class);

    @UiField
    SimplePanel viewContainer;

    @Inject
    public GridControlDescriptorViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getViewContainer() {
        return viewContainer;
    }
}
