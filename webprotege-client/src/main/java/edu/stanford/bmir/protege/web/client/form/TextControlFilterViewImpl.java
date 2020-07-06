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

public class TextControlFilterViewImpl extends Composite implements TextControlFilterView {

    interface TextControlFilterViewImplUiBinder extends UiBinder<HTMLPanel, TextControlFilterViewImpl> {
    }

    private static TextControlFilterViewImplUiBinder ourUiBinder = GWT.create(TextControlFilterViewImplUiBinder.class);
    @UiField
    SimplePanel container;

    @Inject
    public TextControlFilterViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getContainer() {
        return container;
    }
}