package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-08
 */
public class FormViewRowImpl extends Composite implements FormViewRow {

    interface FormViewRowImplUiBinder extends UiBinder<HTMLPanel, FormViewRowImpl> {

    }

    private static FormViewRowImplUiBinder ourUiBinder = GWT.create(FormViewRowImplUiBinder.class);

    @UiField
    HTMLPanel container;

    @Inject
    public FormViewRowImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void add(FormElementView elementView) {
        container.add(elementView);
    }

    @Override
    public void finishRow() {
        if(container.getWidgetCount() > 1) {
            for(int i = 0; i < container.getWidgetCount(); i++) {
                Element element = container.getWidget(i)
                                           .getElement();
                element.addClassName(WebProtegeClientBundle.BUNDLE.style().formGroupMultiCol());
                element.removeClassName(WebProtegeClientBundle.BUNDLE.style().formGroupSingleCol());
            }
        }
        else {
            for(int i = 0; i < container.getWidgetCount(); i++) {
                Element element = container.getWidget(i)
                                           .getElement();
                element.addClassName(WebProtegeClientBundle.BUNDLE.style().formGroupSingleCol());
                element.removeClassName(WebProtegeClientBundle.BUNDLE.style().formGroupMultiCol());
            }
        }
    }
}
