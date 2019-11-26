package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import dagger.multibindings.IntoSet;

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

    @Inject
    public GridViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }
}
