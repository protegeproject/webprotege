package edu.stanford.bmir.protege.web.client.editor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Mar 2018
 */
public class EditorPortletViewImpl extends Composite {
    interface EditorPortletViewImplUiBinder extends UiBinder<com.google.gwt.user.client.ui.HTMLPanel, edu.stanford.bmir.protege.web.client.editor.EditorPortletViewImpl> {
    }

    private static EditorPortletViewImplUiBinder ourUiBinder = GWT.create(EditorPortletViewImplUiBinder.class);

    public EditorPortletViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }
}