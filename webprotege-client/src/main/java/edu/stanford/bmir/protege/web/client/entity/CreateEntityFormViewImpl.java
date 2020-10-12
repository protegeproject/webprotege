package edu.stanford.bmir.protege.web.client.entity;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;

import javax.annotation.Nonnull;
import javax.inject.Inject;

public class CreateEntityFormViewImpl extends Composite implements CreateEntityFormView {

    interface CreateEntityFormViewImplUiBinder extends UiBinder<HTMLPanel, CreateEntityFormViewImpl> {
    }

    private static CreateEntityFormViewImplUiBinder ourUiBinder = GWT.create(CreateEntityFormViewImplUiBinder.class);
    @UiField
    SimplePanel formContainer;

    @Inject
    public CreateEntityFormViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getFormsContainer() {
        return formContainer;
    }
}