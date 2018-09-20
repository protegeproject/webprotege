package edu.stanford.bmir.protege.web.shared.entity;

import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 20 Sep 2018
 */
public class Entity {

    public static boolean isOWLNamedIndividual(@Nonnull OWLEntity entity) {
        return entity.isOWLNamedIndividual();
    }

    @Nonnull
    public static OWLNamedIndividual asOWLNamedIndividual(@Nonnull OWLEntity entity) {
        return entity.asOWLNamedIndividual();
    }
}
