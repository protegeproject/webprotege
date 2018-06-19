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
public class TagCriteriaViewImpl extends Composite implements TagCriteriaView {

    interface TagCriteriaViewImplUiBinder extends UiBinder<HTMLPanel, TagCriteriaViewImpl> {

    }

    private static TagCriteriaViewImplUiBinder ourUiBinder = GWT.create(TagCriteriaViewImplUiBinder.class);

    @UiField
    protected SimplePanel tagCriteriaContainer;

    @Inject
    public TagCriteriaViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getTagCriteriaContainer() {
        return tagCriteriaContainer;
    }
}