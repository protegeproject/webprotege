package edu.stanford.bmir.protege.web.client.perspective;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-02
 */
public interface PerspectivesManagerView extends IsWidget {


    interface ResetPerspectivesHandler {
        void handleResetPerspectives();
    }

    void setResetPerspectivesHandler(@Nonnull ResetPerspectivesHandler handler);

    void displayResetPerspectivesConfirmation(Runnable resetHandler);

    @Nonnull
    AcceptsOneWidget getPerspectivesListContainer();
}
