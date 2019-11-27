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
public class GridFieldDescriptorViewImpl extends Composite implements GridFieldDescriptorView {

    interface GridFieldDescriptorViewImplUiBinder extends UiBinder<HTMLPanel, GridFieldDescriptorViewImpl> {

    }

    private static GridFieldDescriptorViewImplUiBinder ourUiBinder = GWT.create(GridFieldDescriptorViewImplUiBinder.class);

    @UiField
    SimplePanel viewContainer;

    @Inject
    public GridFieldDescriptorViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getViewContainer() {
        return viewContainer;
    }
}
