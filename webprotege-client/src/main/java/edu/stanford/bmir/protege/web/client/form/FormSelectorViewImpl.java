package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import edu.stanford.bmir.protege.web.client.WebProtege;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-27
 */
public class FormSelectorViewImpl extends Composite implements FormSelectorView {

    @Nonnull
    @Override
    public AcceptsOneWidget addFormSelectorItem() {
        SimplePanel simplePanel = new SimplePanel();
        itemContainer.add(simplePanel);
        return simplePanel;
    }

    interface FormSelectorViewImplUiBinder extends UiBinder<HTMLPanel, FormSelectorViewImpl> {

    }

    private static FormSelectorViewImplUiBinder ourUiBinder = GWT.create(FormSelectorViewImplUiBinder.class);

    @UiField
    HTMLPanel itemContainer;

    @Inject
    public FormSelectorViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void clear() {
        itemContainer.clear();
    }
}
