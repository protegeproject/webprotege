package edu.stanford.bmir.protege.web.client.shortform;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-10
 */
public interface DictionaryLanguagePresenter extends HasRequestFocus {

    void start(@Nonnull AcceptsOneWidget container);

    void setDictionaryLanguage(@Nonnull DictionaryLanguage language);

    Optional<DictionaryLanguage> getDictionaryLanguage();

    void requestFocus();
}
