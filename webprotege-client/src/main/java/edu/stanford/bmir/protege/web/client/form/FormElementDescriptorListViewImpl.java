package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-17
 */
public class FormElementDescriptorListViewImpl extends Composite implements FormElementDescriptorListView {

    interface FormElementDescriptorListViewImplUiBinder extends UiBinder<HTMLPanel, FormElementDescriptorListViewImpl> {

    }

    private static FormElementDescriptorListViewImplUiBinder ourUiBinder = GWT.create(
            FormElementDescriptorListViewImplUiBinder.class);

    @UiField
    HTMLPanel descriptorEditorViewContainer;

    private final List<FormElementDescriptorViewHolder> views = new ArrayList<>();

    @Inject
    public FormElementDescriptorListViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void clear() {
        views.clear();
        descriptorEditorViewContainer.clear();
    }

    @Override
    public void addView(@Nonnull FormElementDescriptorViewHolder view) {
        views.add(view);
        descriptorEditorViewContainer.add(view);
    }

    @Override
    public void removeView(@Nonnull FormElementDescriptorViewHolder viewHolder) {
        views.remove(viewHolder);
        descriptorEditorViewContainer.remove(viewHolder);
    }

    @Override
    public void moveUp(@Nonnull FormElementDescriptorViewHolder viewHolder) {
        int fromIndex = views.indexOf(viewHolder);
        int toIndex = fromIndex - 1;
        if(toIndex > -1) {
            Collections.swap(views, fromIndex, toIndex);
            refill(viewHolder);
        }
    }

    @Override
    public void moveDown(@Nonnull FormElementDescriptorViewHolder viewHolder) {
        int fromIndex = views.indexOf(viewHolder);
        int toIndex = fromIndex + 1;
        if(toIndex < views.size()) {
            Collections.swap(views, fromIndex, toIndex);
            refill(viewHolder);
        }
    }

    private void refill(FormElementDescriptorViewHolder viewHolder) {
        descriptorEditorViewContainer.clear();
        views.forEach(view -> descriptorEditorViewContainer.add(view));
        viewHolder.scrollIntoView();
    }
}
