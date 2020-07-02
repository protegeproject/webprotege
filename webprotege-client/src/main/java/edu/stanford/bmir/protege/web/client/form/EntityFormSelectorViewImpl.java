package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-16
 */
public class EntityFormSelectorViewImpl extends Composite implements EntityFormSelectorView {

    interface EntityFormSelectorViewImplUiBinder extends UiBinder<HTMLPanel, EntityFormSelectorViewImpl> {

    }

    private static EntityFormSelectorViewImplUiBinder ourUiBinder = GWT.create(EntityFormSelectorViewImplUiBinder.class);

    @UiField
    SimplePanel container;

    @Inject
    public EntityFormSelectorViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void clear() {

    }

    @Nonnull
    @Override
    public AcceptsOneWidget getSelectorCriteriaContainer() {
        return container;
    }
}
