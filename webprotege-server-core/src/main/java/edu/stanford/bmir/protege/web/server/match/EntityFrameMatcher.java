package edu.stanford.bmir.protege.web.server.match;

import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Jun 2018
 */
public interface EntityFrameMatcher {

    boolean matches(@Nonnull OWLEntity entity);
}
