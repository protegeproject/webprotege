package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-20
 */
public class FormStackViewImpl extends Composite implements FormStackView {

    interface FormStackViewImplUiBinder extends UiBinder<HTMLPanel, FormStackViewImpl> {

    }

    private static FormStackViewImplUiBinder ourUiBinder = GWT.create(FormStackViewImplUiBinder.class);

    @UiField
    FlowPanel container;

    @Inject
    public FormStackViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Nonnull
    @Override
    public AcceptsOneWidget addContainer() {
        FormStackContainer simplePanel = new FormStackContainer();
        container.add(simplePanel);
        return simplePanel;
    }

    @Override
    public void clear() {
        container.clear();
    }
}
