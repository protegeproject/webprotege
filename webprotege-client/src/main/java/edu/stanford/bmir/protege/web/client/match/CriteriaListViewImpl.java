package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Jun 2018
 */
public class CriteriaListViewImpl extends Composite implements CriteriaListView {

    @Nonnull
    private AddCriteriaHandler addCriteriaHandler = () -> {};

    @Nonnull
    private RemoveCriteriaHandler removeCriteriaHandler = (index) -> {};

    interface CriteriaListViewImplUiBinder extends UiBinder<HTMLPanel, CriteriaListViewImpl> {

    }

    private static CriteriaListViewImplUiBinder ourUiBinder = GWT.create(CriteriaListViewImplUiBinder.class);

    @UiField
    FlowPanel criteriaViewsContainer;

    @UiField
    Button addButton;

    @Inject
    public CriteriaListViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }


    @UiHandler("addButton")
    public void addButtonClicked(ClickEvent event) {
        addCriteriaHandler.handleAddCriteria();
    }

    @Override
    public int getCriteriaCount() {
        return criteriaViewsContainer.getWidgetCount();
    }

    @Override
    public void setAddCriteriaHandler(@Nonnull AddCriteriaHandler handler) {
        this.addCriteriaHandler = checkNotNull(handler);
    }

    @Override
    public void setRemoveCriteriaHandler(@Nonnull RemoveCriteriaHandler handler) {
        this.removeCriteriaHandler = checkNotNull(handler);
    }

    @Override
    public void addCriteriaView(@Nonnull CriteriaListCriteriaViewContainer viewContainer) {
        criteriaViewsContainer.add(viewContainer);
    }

    @Override
    public void removeCriteriaView(int index) {
        criteriaViewsContainer.remove(index);
    }
}