package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-20
 */
public class FormEditorViewImpl extends Composite implements FormEditorView {

    interface FormEditorViewImplUiBinder extends UiBinder<HTMLPanel, FormEditorViewImpl> {

    }

    private static FormEditorViewImplUiBinder ourUiBinder = GWT.create(FormEditorViewImplUiBinder.class);

    @Inject
    public FormEditorViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }


}
