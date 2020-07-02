package edu.stanford.bmir.protege.web.server.owlapi;

import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-17
 */
public class ConjunctSet {

    @Nonnull
    public static Stream<OWLClassExpression> asConjuncts(@Nonnull OWLClassExpression classExpression) {
        if(classExpression instanceof OWLObjectIntersectionOf) {
            return ((OWLObjectIntersectionOf) classExpression).getOperandsAsList().stream();
        }
        else {
            return Stream.of(classExpression);
        }
    }
}
