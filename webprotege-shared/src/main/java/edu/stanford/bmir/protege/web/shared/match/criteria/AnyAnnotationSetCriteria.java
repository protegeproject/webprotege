package edu.stanford.bmir.protege.web.shared.match.criteria;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Jun 2018
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("AnyAnnotationSet")
public abstract class AnyAnnotationSetCriteria implements AnnotationSetCriteria {

    @JsonCreator
    @Nonnull
    public static AnyAnnotationSetCriteria get() {
        return new AutoValue_AnyAnnotationSetCriteria();
    }
}
