package edu.stanford.bmir.protege.web.client.entity;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26 Sep 2018
 */
public class MergeEntitiesViewImpl extends Composite implements MergeEntitiesView {

    interface MergeEntitiesViewImplUiBinder extends UiBinder<HTMLPanel, MergeEntitiesViewImpl> {

    }

    private static MergeEntitiesViewImplUiBinder ourUiBinder = GWT.create(MergeEntitiesViewImplUiBinder.class);

    @UiField
    SimplePanel container;

    @Inject
    public MergeEntitiesViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getHierarchyFieldContainer() {
        return container;
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        if(container.getWidget() instanceof HasRequestFocus) {
            ((HasRequestFocus) container.getWidget()).requestFocus();
        }
    }
}