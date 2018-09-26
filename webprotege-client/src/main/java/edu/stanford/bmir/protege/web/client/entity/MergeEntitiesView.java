package edu.stanford.bmir.protege.web.client.entity;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26 Sep 2018
 */
public interface MergeEntitiesView extends IsWidget {

    @Nonnull
    AcceptsOneWidget getHierarchyFieldContainer();
}
