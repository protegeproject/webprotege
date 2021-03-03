package edu.stanford.bmir.protege.web.shared.crud.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-11-01
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class GeneratedAnnotationsSettings {

    public static final String DESCRIPTORS = "descriptors";


    @JsonCreator
    public static GeneratedAnnotationsSettings get(@JsonProperty(DESCRIPTORS) ImmutableList<GeneratedAnnotationDescriptor> descriptors) {
        return new AutoValue_GeneratedAnnotationsSettings(descriptors);
    }

    public static GeneratedAnnotationsSettings empty() {
        return get(ImmutableList.of());
    }

    @JsonProperty(DESCRIPTORS)
    @Nonnull
    public abstract ImmutableList<GeneratedAnnotationDescriptor> getDescriptors();

}
