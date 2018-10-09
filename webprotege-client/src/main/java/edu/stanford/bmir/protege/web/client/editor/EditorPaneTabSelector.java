package edu.stanford.bmir.protege.web.client.editor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 9 Oct 2018
 */
public class EditorPaneTabSelector extends Composite {

    interface EditorPaneTabSelectorUiBinder extends UiBinder<HTMLPanel, EditorPaneTabSelector> {

    }

    private static EditorPaneTabSelectorUiBinder ourUiBinder = GWT.create(EditorPaneTabSelectorUiBinder.class);

    public EditorPaneTabSelector() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }
}