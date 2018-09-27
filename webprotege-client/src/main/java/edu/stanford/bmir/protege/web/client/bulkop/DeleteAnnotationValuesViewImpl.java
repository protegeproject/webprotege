package edu.stanford.bmir.protege.web.client.bulkop;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26 Sep 2018
 */
public class DeleteAnnotationValuesViewImpl extends Composite {

    interface DeleteAnnotationValuesViewImplUiBinder extends UiBinder<com.google.gwt.user.client.ui.HTMLPanel, edu.stanford.bmir.protege.web.client.bulkop.DeleteAnnotationValuesViewImpl> {

    }

    private static DeleteAnnotationValuesViewImplUiBinder ourUiBinder = GWT.create(DeleteAnnotationValuesViewImplUiBinder.class);

    public DeleteAnnotationValuesViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }
}