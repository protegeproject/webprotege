package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-23
 */
public interface EntityFormStackView extends IsWidget {

    interface EnterEditModeHandler {
        void handleEnterEditMode();
    }

    interface ApplyEditsHandler {
        void handleApplyEdits();
    }

    interface CancelEditsHandler {
        void handleCancelEdits();
    }

    @Nonnull
    AcceptsOneWidget getFormStackContainer();

    @Nonnull
    AcceptsOneWidget getLangTagFilterContainer();

    void setEnterEditModeHandler(@Nonnull EnterEditModeHandler enterEditModeHandler);

    void setApplyEditsHandler(@Nonnull ApplyEditsHandler handler);

    void setCancelEditsHandler(@Nonnull CancelEditsHandler handler);

    void setEditButtonVisible(boolean visible);

    void setApplyEditsButtonVisible(boolean visible);

    void setCancelEditsButtonVisible(boolean visible);

}
