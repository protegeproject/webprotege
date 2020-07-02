package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-27
 */
public class FormTabBarViewImpl extends Composite implements FormTabBarView {

    @Override
    public void addView(FormTabView tabView) {
        itemContainer.add(tabView);
    }

    interface FormTabBarViewImplUiBinder extends UiBinder<HTMLPanel, FormTabBarViewImpl> {

    }

    private static FormTabBarViewImplUiBinder ourUiBinder = GWT.create(FormTabBarViewImplUiBinder.class);

    @UiField
    HTMLPanel itemContainer;

    @Inject
    public FormTabBarViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void clear() {
        itemContainer.clear();
    }
}
