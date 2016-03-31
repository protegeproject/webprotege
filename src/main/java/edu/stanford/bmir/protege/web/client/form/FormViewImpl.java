package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public class FormViewImpl extends Composite implements FormView {

    interface FormViewImplUiBinder extends UiBinder<HTMLPanel, FormViewImpl> {

    }

    private static FormViewImplUiBinder ourUiBinder = GWT.create(FormViewImplUiBinder.class);

    @UiField
    FlowPanel holder;

    public FormViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void addFormElementView(FormElementView view) {
        holder.add(view);
    }

    @Override
    public List<FormElementView> getElementViews() {
        List<FormElementView> result = new ArrayList<>();
        for(int i = 0; i < holder.getWidgetCount(); i++) {
            Widget w = holder.getWidget(i);
            if(w instanceof FormElementView) {
                result.add((FormElementView) w);
            }
        }
        return result;

    }

    @Override
    public void clear() {
        holder.clear();
    }
}