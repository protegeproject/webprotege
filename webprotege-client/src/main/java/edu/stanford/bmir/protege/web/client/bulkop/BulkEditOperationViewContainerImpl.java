package edu.stanford.bmir.protege.web.client.bulkop;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Sep 2018
 */
public class BulkEditOperationViewContainerImpl extends Composite implements BulkEditOperationViewContainer {

    interface BulkEditOperationViewContainerImplUiBinder extends UiBinder<HTMLPanel, BulkEditOperationViewContainerImpl> {

    }

    private static BulkEditOperationViewContainerImplUiBinder ourUiBinder = GWT.create(BulkEditOperationViewContainerImplUiBinder.class);

    @UiField
    Label helpTextField;

    @UiField
    SimplePanel container;

    @Inject
    public BulkEditOperationViewContainerImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setHelpText(@Nonnull String helpText) {
        helpTextField.setText(helpText);
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getContainer() {
        return container;
    }

    @Override
    public void requestFocus() {
        if(container.getWidget() instanceof HasRequestFocus) {
            ((HasRequestFocus) container.getWidget()).requestFocus();
        }
    }
}