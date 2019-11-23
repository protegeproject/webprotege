package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
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
}
