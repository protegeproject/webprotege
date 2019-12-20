package edu.stanford.bmir.protege.web.shared.viz;

import edu.stanford.bmir.protege.web.shared.match.criteria.EntityMatchCriteria;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-06
 *
 * A node that matches some entity matching criteria
 */
public interface NodeMatchesCriteria extends EdgeNodeCriteria  {

    @Nonnull
    EntityMatchCriteria getNodeCriteria();
}
