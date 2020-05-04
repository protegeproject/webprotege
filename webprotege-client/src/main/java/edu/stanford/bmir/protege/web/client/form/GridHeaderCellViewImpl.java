package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-27
 */
public class GridHeaderCellViewImpl extends Composite implements GridHeaderCellView {

    interface GridHeaderCellViewImplUiBinder extends UiBinder<HTMLPanel, GridHeaderCellViewImpl> {

    }

    private static GridHeaderCellViewImplUiBinder ourUiBinder = GWT.create(GridHeaderCellViewImplUiBinder.class);

    @UiField
    Label labelField;

    @Inject
    public GridHeaderCellViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setLabel(@Nonnull String label) {
        labelField.setText(checkNotNull(label));
        labelField.setVisible(true);
    }
}
