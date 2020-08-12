package edu.stanford.bmir.protege.web.client.shortform;

import edu.stanford.bmir.protege.web.shared.shortform.AnnotationAssertionDictionaryLanguage;
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
public class AnnotationAssertionDictionaryLanguagePresenterFactory implements DictionaryLanguagePresenterFactory {

    @Nonnull
    private Provider<AnnotationAssertionDictionaryLanguagePresenter> presenterProvider;

    @Inject
    public AnnotationAssertionDictionaryLanguagePresenterFactory(@Nonnull Provider<AnnotationAssertionDictionaryLanguagePresenter> presenterProvider) {
        this.presenterProvider = checkNotNull(presenterProvider);
    }

    @Nonnull
    @Override
    public String getName() {
        return "Annotation";
    }

    @Override
    public boolean isFactoryFor(@Nonnull DictionaryLanguage dictionaryLanguage) {
        return dictionaryLanguage instanceof AnnotationAssertionDictionaryLanguage;
    }

    @Nonnull
    @Override
    public DictionaryLanguagePresenter createPresenter() {
        return presenterProvider.get();
    }
}
