package edu.stanford.bmir.protege.web.client.lang;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguageData;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 31 Jul 2018
 */
public interface DisplayNameSettingsView extends IsWidget {

    void setPrimaryDisplayNameLanguages(@Nonnull ImmutableList<DictionaryLanguage> languages);

    @Nonnull
    ImmutableList<DictionaryLanguage> getPrimaryDisplayNameLanguages();

    void setSecondaryDisplayNameLanguages(@Nonnull ImmutableList<DictionaryLanguage> languages);

    @Nonnull
    ImmutableList<DictionaryLanguage> getSecondaryDisplayNameLanguages();


    void setChangeHandler(@Nonnull ChangeHandler changeHandler);

    interface ChangeHandler {

        void handleDisplayLanguageChanged();
    }
}
