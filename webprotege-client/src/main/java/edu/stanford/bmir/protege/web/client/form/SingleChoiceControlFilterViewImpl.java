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

public class SingleChoiceControlFilterViewImpl extends Composite implements SingleChoiceControlFilterView {

    interface SingleChoiceControlFilterViewImplUiBinder extends UiBinder<HTMLPanel, SingleChoiceControlFilterViewImpl> {
    }

    private static SingleChoiceControlFilterViewImplUiBinder ourUiBinder = GWT.create(
            SingleChoiceControlFilterViewImplUiBinder.class);
    @UiField
    SimplePanel container;

    @Inject
    public SingleChoiceControlFilterViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getContainer() {
        return container;
    }
}