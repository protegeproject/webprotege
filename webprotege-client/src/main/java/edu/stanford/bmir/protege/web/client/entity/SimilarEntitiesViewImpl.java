package edu.stanford.bmir.protege.web.client.entity;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

import javax.annotation.Nonnull;

public class SimilarEntitiesViewImpl extends Composite implements SimilarEntitiesView {

    interface SimilarEntitiesViewImplUiBinder extends UiBinder<HTMLPanel, SimilarEntitiesViewImpl> {
    }

    private static SimilarEntitiesViewImplUiBinder ourUiBinder = GWT.create(SimilarEntitiesViewImplUiBinder.class);

    @Nonnull
    public SimilarEntitiesViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }
}