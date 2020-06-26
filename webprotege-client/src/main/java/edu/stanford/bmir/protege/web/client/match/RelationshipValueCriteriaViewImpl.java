package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.app.ApplicationInitManager;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-23
 */
public class RelationshipValueCriteriaViewImpl extends Composite implements RelationshipValueCriteriaView {

    private Runnable changeHandler = () -> {};

    interface RelationshipValueCriteriaViewImplUiBinder extends UiBinder<HTMLPanel, RelationshipValueCriteriaViewImpl> {

    }

    private static RelationshipValueCriteriaViewImplUiBinder ourUiBinder = GWT.create(
            RelationshipValueCriteriaViewImplUiBinder.class);

    @UiField
    SimplePanel container;

    @UiField
    ListBox valueMatchTypeSelector;

    @Inject
    public RelationshipValueCriteriaViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        valueMatchTypeSelector.addChangeHandler(event -> changeHandler.run());
    }


    @Override
    public void setValueMatchTypeChangedHandler(@Nonnull Runnable runnable) {
        changeHandler = checkNotNull(runnable);
    }

    @Override
    public void setValueMatchType(@Nonnull RelationshipValueMatchType matchType) {
        valueMatchTypeSelector.setSelectedIndex(matchType.ordinal());
    }

    @Nonnull
    @Override
    public RelationshipValueMatchType getValueMatchType() {
        int selIndex = valueMatchTypeSelector.getSelectedIndex();
        if(selIndex == -1) {
            return RelationshipValueMatchType.ANY_VALUE;
        }
        else {
            return RelationshipValueMatchType.values()[selIndex];
        }
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getContainer() {
        return container;
    }
}
