package edu.stanford.bmir.protege.web.server.trigger;

import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Jun 2018
 */
public interface TriggerAction<C> {

    C begin();

    void execute(@Nonnull OWLEntity entity, C context);

    void end(@Nonnull C context);
}
