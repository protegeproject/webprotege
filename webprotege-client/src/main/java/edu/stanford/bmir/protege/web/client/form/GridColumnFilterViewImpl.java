package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

public class GridColumnFilterViewImpl extends Composite implements GridColumnFilterView {

    interface GridColumnFilterViewImplUiBinder extends UiBinder<HTMLPanel, GridColumnFilterViewImpl> {
    }

    private static GridColumnFilterViewImplUiBinder ourUiBinder = GWT.create(GridColumnFilterViewImplUiBinder.class);

    @UiField
    SimplePanel container;

    @Inject
    public GridColumnFilterViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getFilterContainer() {
        return container;
    }
}