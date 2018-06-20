package edu.stanford.bmir.protege.web.client.tag;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Jun 2018
 */
public interface TagCriteriaViewContainer extends IsWidget {

    interface RemoveHandler {
        void handleRemove();
    }

    @Nonnull
    AcceptsOneWidget getViewContainer();

    void setRemoveHandler(RemoveHandler removeHandler);
}

