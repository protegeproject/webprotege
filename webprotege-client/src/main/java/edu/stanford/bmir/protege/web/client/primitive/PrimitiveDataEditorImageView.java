package edu.stanford.bmir.protege.web.client.primitive;

import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10 Apr 2018
 */
public interface PrimitiveDataEditorImageView extends IsWidget {

    interface DismissHandler {
        void handleDismissImageView();
    }

    interface ActivateEditingHandler {
        void handleActiveEditing();
    }

    void setImageUrl(@Nonnull String url);
}
