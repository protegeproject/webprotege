package edu.stanford.bmir.protege.web.client.library.tokenfield;

import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-14
 */
public interface TokenView extends IsWidget {

    interface RemoveTokenRequestHandler {
        void handleRemoveTokenRequest();
    }

    void setLabel(@Nonnull String label);

    void setRemoveTokenRequestHandler(@Nonnull RemoveTokenRequestHandler handler);
}
