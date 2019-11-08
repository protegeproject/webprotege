package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.form.field.ElementRun;

import javax.inject.Inject;
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
    HTMLPanel holder;

    FlowPanel currentRun;

    private List<FormElementView> elementViews = new ArrayList<>();

    @Inject
    public FormViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void addFormElementView(FormElementView view,
                                   ElementRun elementRun) {

        if(currentRun == null || elementRun.isStart()) {
            currentRun = new FlowPanel();
            Style style = currentRun.getElement()
                                    .getStyle();
            style.setProperty("display", "flex");
            style.setProperty("boxSizing", "borderBox");
            style.setProperty("flexDirection", "row");
            style.setProperty("justifyContent", "flexStart");
            holder.add(currentRun);
        }
        currentRun.add(view);
        elementViews.add(view);
        if(currentRun.getWidgetCount() > 1) {
            for(int i = 0; i < currentRun.getWidgetCount(); i++) {
                Element element = currentRun.getWidget(i)
                                            .getElement();
                element.addClassName(WebProtegeClientBundle.BUNDLE.style().formGroupMultiCol());
                element.removeClassName(WebProtegeClientBundle.BUNDLE.style().formGroupSingleCol());
            }
        }
        else {
            for(int i = 0; i < currentRun.getWidgetCount(); i++) {
                Element element = currentRun.getWidget(i)
                                            .getElement();
                element.addClassName(WebProtegeClientBundle.BUNDLE.style().formGroupSingleCol());
                element.removeClassName(WebProtegeClientBundle.BUNDLE.style().formGroupMultiCol());
            }
        }
    }

    @Override
    public List<FormElementView> getElementViews() {
        return new ArrayList<>(elementViews);

    }

    @Override
    public void clear() {
        holder.clear();
    }
}
