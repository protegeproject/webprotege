package edu.stanford.bmir.protege.web.server.match;

import edu.stanford.bmir.protege.web.shared.frame.PlainPropertyValue;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValue;
import edu.stanford.bmir.protege.web.shared.match.criteria.RelationshipCriteria;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-02
 */
public interface RelationshipMatcherFactory {

    Matcher<PlainPropertyValue> getRelationshipMatcher(@Nonnull RelationshipCriteria relationshipCriteria);
}
