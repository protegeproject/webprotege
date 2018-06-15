package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Jun 2018
 */
public class CriteriaListViewViewContainerImpl extends Composite implements CriteriaListCriteriaViewContainer {

    interface CriteriaListViewViewContainerImplUiBinder extends UiBinder<HTMLPanel, CriteriaListViewViewContainerImpl> {

    }

    private static CriteriaListViewViewContainerImplUiBinder ourUiBinder = GWT.create(CriteriaListViewViewContainerImplUiBinder.class);

    @UiField
    SimplePanel viewContainer;

    @UiField
    Button removeButton;

    private RemoveHandler removeHandler = () -> {};

    @Inject
    public CriteriaListViewViewContainerImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @UiHandler("removeButton")
    public void removeButtonClick(ClickEvent event) {
        removeHandler.handleRemoveCriteriaView();
    }

    @Override
    public void setRemoveButtonVisible(boolean visible) {
        Style.Visibility visibility = visible ? Style.Visibility.VISIBLE : Style.Visibility.HIDDEN;
        removeButton.getElement().getStyle().setVisibility(visibility);
    }

    @Override
    public void setRemoveHandler(@Nonnull RemoveHandler removeHandler) {
        this.removeHandler = checkNotNull(removeHandler);
    }

    @Override
    public void setWidget(IsWidget w) {
        viewContainer.setWidget(checkNotNull(w));
    }
}