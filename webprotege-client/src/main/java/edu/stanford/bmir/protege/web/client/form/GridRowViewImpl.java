package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-25
 */
public class GridRowViewImpl extends Composite {

    interface GridRowViewImplUiBinder extends UiBinder<HTMLPanel, GridRowViewImpl> {

    }

    private static GridRowViewImplUiBinder ourUiBinder = GWT.create(GridRowViewImplUiBinder.class);

    public GridRowViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }
}
