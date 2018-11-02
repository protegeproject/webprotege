package edu.stanford.bmir.protege.web.server.util;

import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 1 Nov 2018
 */
public class Named {

    public static boolean isNamedIndividual(@Nonnull OWLIndividual individual) {
        return individual.isNamed();
    }

    public static boolean isAnonymousIndividual(@Nonnull OWLIndividual individual) {
        return individual.isAnonymous();
    }

    public static boolean isNamedClass(@Nonnull OWLClassExpression classExpression) {
        return !classExpression.isAnonymous();
    }

    public static boolean isNamedProperty(@Nonnull OWLObjectPropertyExpression propertyExpression) {
        return !propertyExpression.isAnonymous();
    }

    public static boolean isInverseProperty(@Nonnull OWLObjectPropertyExpression propertyExpression) {
        return propertyExpression.isAnonymous();
    }

}
