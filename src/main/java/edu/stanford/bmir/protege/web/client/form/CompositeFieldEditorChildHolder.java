package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
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

    public void setFlexGrow(double grow) {
        getElement().getStyle().setProperty("flexGrow", String.valueOf(grow));
    }

    public void setFlexShrink(double shrink) {
        getElement().getStyle().setProperty("flexGrow", String.valueOf(shrink));
    }

    public void setWidth(double width) {
        getElement().getStyle().setWidth(width, Style.Unit.PX);
    }
}