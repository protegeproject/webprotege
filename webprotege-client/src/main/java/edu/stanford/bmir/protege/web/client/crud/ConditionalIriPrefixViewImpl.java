package edu.stanford.bmir.protege.web.client.crud;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeHierarchyPositionCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-07
 */
public class ConditionalIriPrefixViewImpl extends Composite implements ConditionalIriPrefixView {

    interface ConditionalIriPrefixesViewImplUiBinder extends UiBinder<HTMLPanel, ConditionalIriPrefixViewImpl> {

    }

    private static ConditionalIriPrefixesViewImplUiBinder ourUiBinder = GWT.create(
            ConditionalIriPrefixesViewImplUiBinder.class);

    @UiField
    TextBox iriPrefixField;

    @UiField
    SimplePanel criteriaListViewContainer;

    @Inject
    public ConditionalIriPrefixViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setIriPrefix(@Nonnull String iriPrefix) {
        iriPrefixField.setValue(iriPrefix);
    }

    @Nonnull
    @Override
    public String getIriPrefix() {
        return iriPrefixField.getValue().trim();
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getHierarchyPositionCriteriaViewContainer() {
        return criteriaListViewContainer;
    }
}
