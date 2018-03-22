package edu.stanford.bmir.protege.web.client.tag;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Mar 2018
 */
public class TagListViewImpl extends Composite implements TagListView {

    interface TagListViewImplUiBinder extends UiBinder<HTMLPanel, TagListViewImpl> {
    }

    private static TagListViewImplUiBinder ourUiBinder = GWT.create(TagListViewImplUiBinder.class);

    @UiField
    protected HTMLPanel tagViewsContainer;

    @Inject
    public TagListViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void clear() {
        tagViewsContainer.clear();
    }

    @Override
    public void setTagViews(@Nonnull List<TagView> tagViews) {
        tagViewsContainer.clear();
        tagViews.forEach(v -> tagViewsContainer.add(v));
    }
}