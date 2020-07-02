package edu.stanford.bmir.protege.web.shared.frame;

import com.fasterxml.jackson.annotation.*;
import org.semanticweb.owlapi.model.OWLPrimitive;
import org.semanticweb.owlapi.model.OWLProperty;

import javax.annotation.Nonnull;

import static edu.stanford.bmir.protege.web.shared.frame.PlainPropertyAnnotationValue.PROPERTY_ANNOTATION_VALUE;
import static edu.stanford.bmir.protege.web.shared.frame.PlainPropertyClassValue.PROPERTY_CLASS_VALUE;
import static edu.stanford.bmir.protege.web.shared.frame.PlainPropertyDatatypeValue.PROPERTY_DATATYPE_VALUE;
import static edu.stanford.bmir.protege.web.shared.frame.PlainPropertyIndividualValue.PROPERTY_INDIVIDUAL_VALUE;
import static edu.stanford.bmir.protege.web.shared.frame.PlainPropertyLiteralValue.PROPERTY_LITERAL_VALUE;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-03-31
 */
@JsonSubTypes({
        @JsonSubTypes.Type(value = PlainPropertyClassValue.class, name = PROPERTY_CLASS_VALUE),
        @JsonSubTypes.Type(value = PlainPropertyIndividualValue.class, name = PROPERTY_INDIVIDUAL_VALUE),
        @JsonSubTypes.Type(value = PlainPropertyLiteralValue.class, name = PROPERTY_LITERAL_VALUE),
        @JsonSubTypes.Type(value = PlainPropertyDatatypeValue.class, name = PROPERTY_DATATYPE_VALUE),
        @JsonSubTypes.Type(value = PlainPropertyAnnotationValue.class, name = PROPERTY_ANNOTATION_VALUE)
})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public abstract class PlainPropertyValue {

    public static final String PROPERTY = "property";

    public static final String VALUE = "value";

    @JsonProperty(PROPERTY)
    @Nonnull
    public abstract OWLProperty getProperty();

    @JsonProperty(VALUE)
    @Nonnull
    public abstract OWLPrimitive getValue();

    @JsonIgnore
    @Nonnull
    public abstract State getState();

    public abstract <R> R accept(PlainPropertyValueVisitor<R> visitor);

    @JsonIgnore
    public abstract boolean isAnnotation();

    @JsonIgnore
    @Nonnull
    public abstract PlainPropertyValue withState(State state);

    @JsonIgnore
    @Nonnull
    public abstract PropertyValue toPropertyValue(@Nonnull FrameComponentRenderer renderer);

    @JsonIgnore
    public abstract boolean isLogical();

}
