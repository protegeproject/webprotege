package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Jun 2018
 */
public interface CriteriaListCriteriaViewContainer extends AcceptsOneWidget, IsWidget {

    interface RemoveHandler {
        void handleRemoveCriteriaView();
    }

    void setRemoveHandler(@Nonnull RemoveHandler removeHandler);

    void setRemoveButtonVisible(boolean visible);
}
