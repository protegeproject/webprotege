package edu.stanford.bmir.protege.web.shared.match.criteria;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-02
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "match")
@JsonSubTypes({
                      @Type(AnnotationComponentsCriteria.class),
              })
@AutoValue
@GwtCompatible(serializable = true)
public abstract class RelationshipCriteria implements Criteria {

    @Nonnull
    public static RelationshipCriteria get(@Nonnull RelationshipPropertyCriteria propertyCriteria,
                                           @Nonnull RelationshipValueCriteria valueCriteria) {
        return new AutoValue_RelationshipCriteria(propertyCriteria, valueCriteria);
    }

    @JsonProperty("property")
    @Nonnull
    public abstract RelationshipPropertyCriteria getPropertyCriteria();


    @JsonProperty("value")
    @Nonnull
    public abstract RelationshipValueCriteria getValueCriteria();


}
