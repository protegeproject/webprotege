package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.shared.match.criteria.MultiMatchType;

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

    @UiField
    InlineLabel matchTextPrefix;

    @UiField
    ListBox multiMatchTypeField;

    @UiField
    HTMLPanel multiMatchContainer;

    @Inject
    public CriteriaListViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        multiMatchContainer.setVisible(false);
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
    public MultiMatchType getMultiMatchType() {
        return MultiMatchType.valueOf(multiMatchTypeField.getSelectedValue());
    }

    @Override
    public void setMatchTextPrefix(@Nonnull String prefix) {
        matchTextPrefix.setText(checkNotNull(prefix));
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
        if(getCriteriaCount() > 1) {
            multiMatchContainer.setVisible(true);
        }
    }

    @Override
    public void removeCriteriaView(int index) {
        criteriaViewsContainer.remove(index);
        if(getCriteriaCount() == 1) {
            multiMatchContainer.setVisible(false);
        }
    }

    @Override
    public void setMultiMatchType(@Nonnull MultiMatchType multiMatchType) {
        multiMatchTypeField.setSelectedIndex(multiMatchType.ordinal());
    }

    @Override
    public void removeAllCriteriaViews() {
        criteriaViewsContainer.clear();
    }
}