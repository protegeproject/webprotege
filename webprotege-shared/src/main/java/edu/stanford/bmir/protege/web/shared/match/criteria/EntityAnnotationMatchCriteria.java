package edu.stanford.bmir.protege.web.shared.match.criteria;

import edu.stanford.bmir.protege.web.shared.match.AnnotationPresence;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10 Jun 2018
 */
public interface EntityAnnotationMatchCriteria extends EntityMatchCriteria {

    @Nonnull
    OWLAnnotationProperty getProperty();

    @Nonnull
    AnnotationPresence getPresence();
}
