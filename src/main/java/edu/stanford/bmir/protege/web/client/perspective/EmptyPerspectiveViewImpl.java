package edu.stanford.bmir.protege.web.client.perspective;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/02/16
 */
public class EmptyPerspectiveViewImpl extends Composite implements EmptyPerspectiveView {

    interface EmptyPerspectiveViewImplUiBinder extends UiBinder<HTMLPanel, EmptyPerspectiveViewImpl> {

    }

    @UiField
    protected HTMLPanel hasText;

    private static EmptyPerspectiveViewImplUiBinder ourUiBinder = GWT.create(EmptyPerspectiveViewImplUiBinder.class);

    @Inject
    public EmptyPerspectiveViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }


}