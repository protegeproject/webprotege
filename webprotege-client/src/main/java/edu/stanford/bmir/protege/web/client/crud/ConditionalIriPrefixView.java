package edu.stanford.bmir.protege.web.client.crud;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeHierarchyPositionCriteria;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-07
 */
public interface ConditionalIriPrefixView extends IsWidget {

    void setIriPrefix(@Nonnull String iriPrefix);

    @Nonnull
    String getIriPrefix();

    @Nonnull
    AcceptsOneWidget getHierarchyPositionCriteriaViewContainer();
}
