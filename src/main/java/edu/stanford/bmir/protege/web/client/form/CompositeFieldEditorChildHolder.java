package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/04/16
 */
public class CompositeFieldEditorChildHolder extends Composite {

    interface CompositeFieldEditorChildHolderUiBinder extends UiBinder<HTMLPanel, CompositeFieldEditorChildHolder> {

    }

    private static CompositeFieldEditorChildHolderUiBinder ourUiBinder = GWT.create(CompositeFieldEditorChildHolderUiBinder.class);

    @UiField
    SimplePanel holder;

    public CompositeFieldEditorChildHolder() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    public void setEditor(IsWidget widget) {
        holder.setWidget(widget);
    }
}