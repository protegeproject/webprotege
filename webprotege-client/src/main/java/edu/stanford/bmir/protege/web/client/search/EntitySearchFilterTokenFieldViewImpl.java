package edu.stanford.bmir.protege.web.client.search;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;

import javax.annotation.Nonnull;
import javax.inject.Inject;

public class EntitySearchFilterTokenFieldViewImpl extends Composite implements EntitySearchFilterTokenFieldView {

    interface EntitySearchFilterTokenFieldViewImplUiBinder extends UiBinder<HTMLPanel, EntitySearchFilterTokenFieldViewImpl> {
    }

    private static EntitySearchFilterTokenFieldViewImplUiBinder ourUiBinder = GWT.create(
            EntitySearchFilterTokenFieldViewImplUiBinder.class);
    @UiField
    SimplePanel tokenFieldContainer;

    @Inject
    public EntitySearchFilterTokenFieldViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getTokenFieldContainer() {
        return tokenFieldContainer;
    }
}