package edu.stanford.bmir.protege.web.client.shortform;

import edu.stanford.bmir.protege.web.shared.shortform.AnnotationAssertionPathDictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-10
 */
public class AnnotationAssertionPathDictionaryLanguagePresenterFactory implements DictionaryLanguagePresenterFactory {

    @Nonnull
    private final Provider<AnnotationAssertionPathDictionaryLanguagePresenter> presenterProvider;

    @Inject
    public AnnotationAssertionPathDictionaryLanguagePresenterFactory(@Nonnull Provider<AnnotationAssertionPathDictionaryLanguagePresenter> presenterProvider) {
        this.presenterProvider = checkNotNull(presenterProvider);
    }

    @Nonnull
    @Override
    public String getName() {
        return "Annotation path";
    }

    @Override
    public boolean isFactoryFor(@Nonnull DictionaryLanguage dictionaryLanguage) {
        return dictionaryLanguage instanceof AnnotationAssertionPathDictionaryLanguage;
    }

    @Nonnull
    @Override
    public DictionaryLanguagePresenter createPresenter() {
        return presenterProvider.get();
    }
}
