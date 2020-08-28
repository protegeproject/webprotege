package edu.stanford.bmir.protege.web.client.shortform;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.shortform.OboIdDictionaryLanguage;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-10
 */
public class OboIdDictionaryLanguagePresenter implements DictionaryLanguagePresenter {

    @Nonnull
    private final DictionaryLanguageBlankView view;

    @Inject
    public OboIdDictionaryLanguagePresenter(@Nonnull DictionaryLanguageBlankView view) {
        this.view = checkNotNull(view);
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
    }

    @Override
    public void setDictionaryLanguage(@Nonnull DictionaryLanguage language) {

    }

    @Override
    public Optional<DictionaryLanguage> getDictionaryLanguage() {
        return Optional.of(OboIdDictionaryLanguage.get());
    }

    @Override
    public void requestFocus() {

    }
}
