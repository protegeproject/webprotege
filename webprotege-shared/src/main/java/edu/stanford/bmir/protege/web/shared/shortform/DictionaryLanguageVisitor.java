package edu.stanford.bmir.protege.web.shared.shortform;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-05
 */
public interface DictionaryLanguageVisitor<R> {

    default R getDefault() {
        return null;
    }

    default R visit(@Nonnull LocalNameDictionaryLanguage language) {
        return getDefault();
    };

    default R visit(@Nonnull OboIdDictionaryLanguage language) {
        return getDefault();
    }

    default R visit(@Nonnull AnnotationAssertionDictionaryLanguage language) {
        return getDefault();
    }

    default R visit(@Nonnull AnnotationAssertionPathDictionaryLanguage language) {
        return getDefault();
    }

    default R visit(@Nonnull PrefixedNameDictionaryLanguage language) {
        return getDefault();
    }


}
