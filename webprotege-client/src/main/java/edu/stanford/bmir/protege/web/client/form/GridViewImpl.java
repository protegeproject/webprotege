package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import dagger.multibindings.IntoSet;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-25
 */
public class GridViewImpl extends Composite implements GridView {

    interface GridViewImplUiBinder extends UiBinder<HTMLPanel, GridViewImpl> {

    }

    private static GridViewImplUiBinder ourUiBinder = GWT.create(GridViewImplUiBinder.class);

    @UiField
    FlowPanel rowContainer;

    @Inject
    public GridViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Nonnull
    @Override
    public AcceptsOneWidget addRow() {
        SimplePanel row = new SimplePanel();
        rowContainer.add(row);
        return row;
    }

    @Override
    public void clear() {
        rowContainer.clear();
    }
}
