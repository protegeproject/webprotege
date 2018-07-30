package edu.stanford.bmir.protege.web.client.lang;

import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 29 Jul 2018
 */
public interface PreferredLanguageView extends IsWidget {

    interface ChangeHandler {
        void handlePreferredLanguageChanged();
    }

    @Nonnull
    String getLanguage();

    void setLanguage(@Nonnull String language);

    void setChangeHandler(@Nonnull ChangeHandler handler);

}
