package edu.stanford.bmir.protege.web.shared.shortform;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-05
 */
public interface DictionaryLanguageVisitor<R> {

    default R visit(@Nonnull LocalNameDictionaryLanguage language) {
        return null;
    };

    default R visit(@Nonnull OboIdDictionaryLanguage language) {
        return null;
    }

    default R visit(@Nonnull AnnotationAssertionDictionaryLanguage language) {
        return null;
    }

    default R visit(@Nonnull AnnotationAssertionPathDictionaryLanguage language) {
        return null;
    }

    default R visit(@Nonnull PrefixedNameDictionaryLanguage language) {
        return null;
    }


}
