package edu.stanford.bmir.protege.web.shared.frame;

import com.google.auto.value.AutoValue;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-02
 */
@AutoValue
public abstract class ClassFrameTranslationOptions {

    public static ClassFrameTranslationOptions defaultOptions() {
        return get(AncestorsTreatment.EXCLUDE_ANCESTORS,
                   RelationshipTranslationOptions.get(
                           RelationshipTranslationOptions.allOutgoingRelationships(),
                           RelationshipTranslationOptions.noIncomingRelationships(),
                           RelationshipTranslationOptions.RelationshipMinification.NON_MINIMIZED_RELATIONSHIPS
                   ));
    }

    public enum AncestorsTreatment {
        INCLUDE_ANCESTORS,
        EXCLUDE_ANCESTORS;
    }

    public static ClassFrameTranslationOptions get(@Nonnull AncestorsTreatment ancestorsTreatment,
                                                   @Nonnull RelationshipTranslationOptions relationshipTranslationOptions) {
        return new AutoValue_ClassFrameTranslationOptions(ancestorsTreatment, relationshipTranslationOptions);
    }

    public abstract AncestorsTreatment getAncestorsTreatment();

    @Nonnull
    public abstract RelationshipTranslationOptions getRelationshipTranslationOptions();
}
