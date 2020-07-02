package edu.stanford.bmir.protege.web.server.match;

import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeHierarchyPositionCriteria;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-08
 */
public interface HierarchyPositionMatcherFactory {

    @Nonnull
    Matcher<OWLEntity> getHierarchyPositionMatcher(@Nonnull CompositeHierarchyPositionCriteria criteria);
}
