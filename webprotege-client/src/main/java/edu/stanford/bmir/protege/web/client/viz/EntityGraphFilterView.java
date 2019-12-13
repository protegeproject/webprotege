package edu.stanford.bmir.protege.web.client.viz;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-11
 */
public interface EntityGraphFilterView extends IsWidget {

    boolean isActive();

    void setActive(boolean active);

    void setName(@Nonnull String name);

    @Nonnull
    String getName();

    void setDescription(@Nonnull String description);

    @Nonnull
    String getDescription();

    @Nonnull
    AcceptsOneWidget getInclusionCriteriaContainer();

    @Nonnull
    AcceptsOneWidget getExclusionCriteriaContainer();

}
