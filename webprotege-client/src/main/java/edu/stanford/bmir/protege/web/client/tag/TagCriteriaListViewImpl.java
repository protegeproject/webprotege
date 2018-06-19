package edu.stanford.bmir.protege.web.client.tag;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Jun 2018
 */
public class TagCriteriaListViewImpl extends Composite implements TagCriteriaListView {

    interface TagCriteriaListViewImplUiBinder extends UiBinder<HTMLPanel, TagCriteriaListViewImpl> {

    }

    private static TagCriteriaListViewImplUiBinder ourUiBinder = GWT.create(TagCriteriaListViewImplUiBinder.class);

    @UiField
    HTMLPanel tagCriteriaListContainer;

    @Inject
    public TagCriteriaListViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void clearView() {
        tagCriteriaListContainer.clear();
    }

    @Override
    public void addTagCriteriaViewContainer(@Nonnull TagCriteriaViewContainer container) {
        tagCriteriaListContainer.add(container);
    }
}