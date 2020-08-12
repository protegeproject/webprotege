package edu.stanford.bmir.protege.web.client.lang;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.lang.DictionaryLanguageUsage;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguageData;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Jul 2018
 */
public interface DefaultDisplayNameSettingsView extends IsWidget {

    interface ResetLanguagesHandler {
        void handleResetLanguages();
    }

    @Nonnull
    ImmutableList<DictionaryLanguage> getPrimaryLanguages();

    void setPrimaryLanguages(@Nonnull List<DictionaryLanguage> primaryLanguages);

    void setResetLanguagesHandler(@Nonnull ResetLanguagesHandler handler);
}
