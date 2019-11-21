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
 * 2019-11-21
 */
public class SubFormFieldDescriptorViewImpl extends Composite implements SubFormFieldDescriptorView {

    interface SubFormFieldDescriptorViewImplUiBinder extends UiBinder<HTMLPanel, SubFormFieldDescriptorViewImpl> {

    }

    private static SubFormFieldDescriptorViewImplUiBinder ourUiBinder = GWT.create(
            SubFormFieldDescriptorViewImplUiBinder.class);

    @UiField
    SimplePanel subFormContainer;

    @Inject
    public SubFormFieldDescriptorViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getSubFormContainer() {
        return subFormContainer;
    }

    @Override
    public void clear() {

    }
}
