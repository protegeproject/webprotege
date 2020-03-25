package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-23
 */
public interface RelationshipValueCriteriaView extends IsWidget {

    void setValueMatchTypeChangedHandler(@Nonnull Runnable runnable);

    void setValueMatchType(@Nonnull RelationshipValueMatchType matchType);

    @Nonnull
    RelationshipValueMatchType getValueMatchType();

    @Nonnull
    AcceptsOneWidget getContainer();
}
