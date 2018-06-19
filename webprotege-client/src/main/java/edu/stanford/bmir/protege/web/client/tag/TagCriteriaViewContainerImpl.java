package edu.stanford.bmir.protege.web.client.tag;

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
 * 19 Jun 2018
 */
public class TagCriteriaViewContainerImpl extends Composite implements TagCriteriaViewContainer {

    interface TagCriteriaViewContainerImplUiBinder extends UiBinder<HTMLPanel, TagCriteriaViewContainerImpl> {

    }

    private static TagCriteriaViewContainerImplUiBinder ourUiBinder = GWT.create(TagCriteriaViewContainerImplUiBinder.class);

    @UiField
    SimplePanel viewContainer;

    @Inject
    public TagCriteriaViewContainerImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getViewContainer() {
        return viewContainer;
    }
}