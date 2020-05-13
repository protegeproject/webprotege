package edu.stanford.bmir.protege.web.shared.frame;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.Comparator;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-03-31
 */
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = PlainClassFrame.class, name = PlainClassFrame.CLASS_FRAME),
                @JsonSubTypes.Type(value = PlainNamedIndividualFrame.class, name = PlainNamedIndividualFrame.INDIVIDUAL_FRAME),
                @JsonSubTypes.Type(value = PlainObjectPropertyFrame.class, name = PlainObjectPropertyFrame.OBJECT_PROPERTY_FRAME),
                @JsonSubTypes.Type(value = PlainDataPropertyFrame.class, name = PlainDataPropertyFrame.DATA_PROPERTY_FRAME),
                @JsonSubTypes.Type(value = PlainAnnotationPropertyFrame.class, name = PlainAnnotationPropertyFrame.ANNOTATION_PROPERTY_FRAME)
        }
)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public abstract class PlainEntityFrame implements Frame<OWLEntity> {

    public static final String SUBJECT = "subject";

    public static final String PROPERTY_VALUES = "propertyValues";

    public static final String PARENTS = "parents";

    @JsonProperty(SUBJECT)
    @Nonnull
    public abstract OWLEntity getSubject();

    @JsonProperty(PROPERTY_VALUES)
    @Nonnull
    public abstract ImmutableSet<? extends PlainPropertyValue> getPropertyValues();

    @JsonIgnore
    @Nonnull
    public abstract EntityFrame<? extends OWLEntityData> toEntityFrame(FrameComponentRenderer renderer,
                                                                       Comparator<PropertyValue> propertyValueComparator);
}
