package edu.stanford.bmir.protege.web.shared.frame;

import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-02
 */
@AutoValue
public abstract class ClassFrameTranslatorOptions {

    public enum AncestorsTreatment {
        INCLUDE_ANCESTORS,
        EXCLUDE_ANCESTORS;
    }

    public static ClassFrameTranslatorOptions get(@Nonnull AncestorsTreatment ancestorsTreatment,
                                                  @Nonnull RelationshipTranslationOptions relationshipTranslationOptions) {
        return new AutoValue_ClassFrameTranslatorOptions(ancestorsTreatment, relationshipTranslationOptions);
    }

    public abstract AncestorsTreatment getAncestorsTreatment();

    @Nonnull
    public abstract RelationshipTranslationOptions getRelationshipTranslationOptions();
}
