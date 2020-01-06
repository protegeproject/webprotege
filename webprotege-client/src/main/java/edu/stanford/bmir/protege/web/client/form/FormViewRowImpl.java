package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

import javax.inject.Inject;

import static edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle.BUNDLE;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-08
 */
public class FormViewRowImpl extends Composite implements FormViewRow {

    private static FormViewRowImplUiBinder ourUiBinder = GWT.create(FormViewRowImplUiBinder.class);

    @UiField
    HTMLPanel container;

    @Inject
    public FormViewRowImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void add(FormFieldView elementView) {
        int viewCount = container.getWidgetCount();
        if(viewCount == 0) {
            setSingleColStyle(elementView);
        }
        else {
            if(viewCount == 1) {
                Widget firstView = container.getWidget(0);
                setMultiColStyle(firstView);
            }
            setMultiColStyle(elementView);
        }
        container.add(elementView);
    }

    private void setSingleColStyle(IsWidget elementView) {
        elementView.asWidget()
                   .addStyleName(BUNDLE.style()
                                       .formGroupSingleCol());
    }

    private void setMultiColStyle(IsWidget view) {
        view.asWidget().removeStyleName(BUNDLE.style()
                                        .formGroupSingleCol());
        view.asWidget().addStyleName(BUNDLE.style()
                                     .formGroupMultiCol());
    }

    interface FormViewRowImplUiBinder extends UiBinder<HTMLPanel, FormViewRowImpl> {

    }
}
