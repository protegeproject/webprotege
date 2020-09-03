package edu.stanford.bmir.protege.web.client.perspective;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;

import javax.annotation.Nonnull;
import javax.inject.Inject;

public class PerspectivesManagerViewImpl extends Composite implements PerspectivesManagerView {

    interface PerspectivesManagerViewImplUiBinder extends UiBinder<HTMLPanel, PerspectivesManagerViewImpl> {
    }

    private static PerspectivesManagerViewImplUiBinder ourUiBinder = GWT.create(PerspectivesManagerViewImplUiBinder.class);

    @UiField
    SimplePanel container;

    @Inject
    public PerspectivesManagerViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getPerspectivesListContainer() {
        return container;
    }
}