package edu.stanford.bmir.protege.web.client.perspective;

import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/02/16
 */
public interface EmptyPerspectiveView extends IsWidget {

    interface AddViewHandler {
        void handleAddView();
    }

    void setAddViewHandler(@Nonnull AddViewHandler addViewHandler);
}
