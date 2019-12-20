package edu.stanford.bmir.protege.web.shared.match.criteria;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10 Jun 2018
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "match")
@JsonSubTypes({
        @Type(EntityIsDeprecatedCriteria.class),
        @Type(EntityIsNotDeprecatedCriteria.class),
        @Type(EntityAnnotationCriteria.class),
        @Type(EntityHasConflictingBooleanAnnotationValuesCriteria.class),
        @Type(EntityHasNonUniqueLangTagsCriteria.class),
        @Type(EntityAnnotationValuesAreNotDisjointCriteria.class),
        @Type(EntityTypeIsOneOfCriteria.class),
        @Type(IsNotBuiltInEntityCriteria.class),
        @Type(SubClassOfCriteria.class),
        @Type(InstanceOfCriteria.class),
        @Type(CompositeRootCriteria.class),
        @Type(EntityRelationshipCriteria.class),
        @Type(EntityIsCriteria.class)
})
public interface EntityMatchCriteria extends RootCriteria {

}
