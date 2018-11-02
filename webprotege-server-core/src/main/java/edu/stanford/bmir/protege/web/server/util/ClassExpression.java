package edu.stanford.bmir.protege.web.server.util;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLRestriction;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Sep 2018
 */
public class ClassExpression {

    public static boolean isOWLClass(@Nonnull OWLClassExpression classExpression) {
        return !classExpression.isAnonymous();
    }

    @Nonnull
    public static OWLClass asOWLClass(@Nonnull OWLClassExpression classExpression) {
        return classExpression.asOWLClass();
    }

    public static Boolean isNotOwlThing(@Nonnull OWLClassExpression classExpression) {
        return !classExpression.isOWLThing();
    }
}
