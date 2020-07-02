package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

public class GridColumnFilterContainerImpl extends Composite implements GridColumnFilterContainer {

    interface GridColumnFilterContainerImplUiBinder extends UiBinder<HTMLPanel, GridColumnFilterContainerImpl> {
    }

    private static GridColumnFilterContainerImplUiBinder ourUiBinder = GWT.create(GridColumnFilterContainerImplUiBinder.class);

    @UiField
    Label columnNameField;

    @UiField
    SimplePanel container;

    @Inject
    public GridColumnFilterContainerImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setColumnName(@Nonnull String columnName) {
        columnNameField.setText(columnName);
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getFilterContainer() {
        return container;
    }
}