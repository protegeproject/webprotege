package edu.stanford.bmir.protege.web.client.perspective;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

public class PerspectivesManagerViewImpl extends Composite implements PerspectivesManagerView {

    private ResetPerspectivesHandler resetPerspectivesHandler;

    interface PerspectivesManagerViewImplUiBinder extends UiBinder<HTMLPanel, PerspectivesManagerViewImpl> {
    }

    private static PerspectivesManagerViewImplUiBinder ourUiBinder = GWT.create(PerspectivesManagerViewImplUiBinder.class);

    @UiField
    SimplePanel container;

    @UiField
    Button resetPerspectivesButton;

    @Inject
    public PerspectivesManagerViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        resetPerspectivesButton.addClickHandler(this::handleResetPerspectives);
    }

    private void handleResetPerspectives(ClickEvent event) {
        resetPerspectivesHandler.handleResetPerspectives();
    }

    @Override
    public void setResetPerspectivesHandler(@Nonnull ResetPerspectivesHandler handler) {
        resetPerspectivesHandler = checkNotNull(handler);
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getPerspectivesListContainer() {
        return container;
    }
}