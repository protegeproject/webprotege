package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.lang.LanguageMapChangedHandler;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-21
 */
public interface FormsManagerObjectView extends IsWidget {

    interface ShowFormDetailsHandler {
        void handleShowFormDetails();
    }

    void setShowFormDetailsHandler(@Nonnull ShowFormDetailsHandler handler);

    void setLanguageMap(@Nonnull LanguageMap languageMap);

    @Nonnull
    LanguageMap getLanguageMap();

    void setLanguageMapChangedHandler(@Nonnull LanguageMapChangedHandler handler);
}
