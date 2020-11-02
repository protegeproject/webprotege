package edu.stanford.bmir.protege.web.shared.crud.gen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-11-01
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName(IncrementingPatternDescriptor.TYPE_NAME)
public abstract class IncrementingPatternDescriptor implements GeneratedValueDescriptor {

    public static final String TYPE_NAME = "IncrementingPattern";

    public static final String STARTING_VALUE = "startingValue";

    public static final String FORMAT = "format";

    @JsonCreator
    public static IncrementingPatternDescriptor get(@JsonProperty(STARTING_VALUE) int startingValue,
                                                    @JsonProperty(FORMAT) String pattern) {
        return new AutoValue_IncrementingPatternDescriptor(startingValue, pattern);
    }


    @JsonProperty(STARTING_VALUE)
    public abstract int getStartingValue();

    /**
     * Gets the pattern used to generated the value for the annotation.  This should be in
     * the printf format
     */
    @JsonProperty(FORMAT)
    @Nonnull
    public abstract String getFormat();

    @Override
    public void accept(GeneratedValueDescriptorVisitor visitor) {
        visitor.visit(this);
    }
}
