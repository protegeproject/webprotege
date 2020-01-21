package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-20
 */
public class FormStackContainer extends Composite implements AcceptsOneWidget {

    interface FormStackContainerUiBinder extends UiBinder<HTMLPanel, FormStackContainer> {

    }

    private static FormStackContainerUiBinder ourUiBinder = GWT.create(FormStackContainerUiBinder.class);

    @UiField
    HTMLPanel container;

    @Inject
    public FormStackContainer() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setWidget(IsWidget w) {
        container.clear();
        container.add(w);
    }
}
