package edu.stanford.bmir.protege.web.server.match;

import edu.stanford.bmir.protege.web.shared.match.criteria.EntityMatchCriteria;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-15
 */
public interface EntityMatcherFactory {

    @Nonnull
    Matcher<OWLEntity> getEntityMatcher(@Nonnull EntityMatchCriteria criteria);
}
