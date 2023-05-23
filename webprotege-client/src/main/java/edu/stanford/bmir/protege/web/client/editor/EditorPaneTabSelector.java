package edu.stanford.bmir.protege.web.client.editor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 9 Oct 2018
 */
public class EditorPaneTabSelector extends Composite {

    @UiField
    protected Label label;

    interface EditorPaneTabSelectorUiBinder extends UiBinder<HTMLPanel, EditorPaneTabSelector> {

    }

    private static EditorPaneTabSelectorUiBinder ourUiBinder = GWT.create(EditorPaneTabSelectorUiBinder.class);

    public EditorPaneTabSelector() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    public void setLabel(String label) {
        this.label.setText(label);
    }
}