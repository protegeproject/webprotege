package edu.stanford.bmir.protege.web.shared.match.criteria;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-08
 */
@JsonSubTypes({
        @JsonSubTypes.Type(SubClassOfCriteria.class),
        @JsonSubTypes.Type(InstanceOfCriteria.class),
        @JsonSubTypes.Type(CompositeHierarchyPositionCriteria.class)
})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property="match")
public interface HierarchyPositionCriteria extends Criteria {

}
