package edu.stanford.bmir.protege.web.client.shortform;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-10
 */
@Module
public class ShortFormModule {

    @Provides
    DictionaryLanguageSelectorView provideDictionaryLanguageView(DictionaryLanguageSelectorViewImpl impl) {
        return impl;
    }

    @Provides
    AnnotationAssertionDictionaryLanguageView provideAnnotationAssertionDictionaryLanguageView(AnnotationAssertionDictionaryLanguageViewImpl impl) {
        return impl;
    }

    @Provides
    @IntoSet
    DictionaryLanguagePresenterFactory providePrefixedNameDictionaryLanguagePresenterFactory(PrefixedNameDictionaryLanguagePresenterFactory presenterFactory) {
        return presenterFactory;
    }

    @Provides
    @IntoSet
    DictionaryLanguagePresenterFactory provideLocalNameDictionaryLanguagePresenterFactory(LocalNameDictionaryLanguagePresenterFactory presenterFactory) {
        return presenterFactory;
    }

    @Provides
    @IntoSet
    DictionaryLanguagePresenterFactory provideOboIdDictionaryLanguagePresenterFactory(OboIdIDictionaryLanguagePresenterFactory presenterFactory) {
        return presenterFactory;
    }

    @Provides
    @IntoSet
    DictionaryLanguagePresenterFactory provideAnnotationAssertionLanguagePresenterFactory(
            AnnotationAssertionDictionaryLanguagePresenterFactory presenterFactory) {
        return presenterFactory;
    }

    @Provides
    @IntoSet
    DictionaryLanguagePresenterFactory provideAnnotationAssertionPathLanguagePresenterFactory(
            AnnotationAssertionPathDictionaryLanguagePresenterFactory presenterFactory) {
        return presenterFactory;
    }

    @Provides
    AnnotationAssertionPathDictionaryLanguageView provideAnnotationAssertionPathDictionaryLanguageView(AnnotationAssertionPathDictionaryLanguageViewImpl impl) {
        return impl;
    }

    @Provides
    DictionaryLanguageBlankView provideDictionaryLanguageBlankView(DictionaryLanguageBlankViewImpl impl) {
        return impl;
    }
}
