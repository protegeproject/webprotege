package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-18
 */
public class FormElementDescriptorViewHolderImpl extends Composite implements FormElementDescriptorViewHolder {

    interface FormElementDescriptorListViewHolderUiBinder extends UiBinder<HTMLPanel, FormElementDescriptorViewHolderImpl> {

    }

    private static FormElementDescriptorListViewHolderUiBinder ourUiBinder = GWT.create(
            FormElementDescriptorListViewHolderUiBinder.class);

    @UiField
    HTMLPanel container;

    @Inject
    public FormElementDescriptorViewHolderImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setWidget(IsWidget w) {
        container.clear();
        container.add(w);
    }
}
