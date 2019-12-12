package edu.stanford.bmir.protege.web.client.viz;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-11
 */
public interface EntityGraphFilterListItemView extends IsWidget {

    @Nonnull
    AcceptsOneWidget getContainer();

    void setDeleteHandler(Runnable handler);
}
