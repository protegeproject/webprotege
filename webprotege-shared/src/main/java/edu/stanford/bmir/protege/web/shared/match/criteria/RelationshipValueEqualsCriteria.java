package edu.stanford.bmir.protege.web.shared.match.criteria;

import org.semanticweb.owlapi.model.OWLPrimitive;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-04
 */
public interface RelationshipValueEqualsCriteria extends RelationshipValueCriteria {

    @Nonnull
    OWLPrimitive getValue();
}
