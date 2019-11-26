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
 * 2019-11-25
 */
public class GridCellViewImpl extends Composite implements GridCellView {

    interface GridCellViewImplUiBinder extends UiBinder<HTMLPanel, GridCellViewImpl> {

    }

    private static GridCellViewImplUiBinder ourUiBinder = GWT.create(GridCellViewImplUiBinder.class);

    @UiField
    SimplePanel editorContainer;

    @Inject
    public GridCellViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getEditorContainer() {
        return editorContainer;
    }
}
