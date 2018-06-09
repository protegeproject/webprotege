package edu.stanford.bmir.protege.web.server.match;

import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLObject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 8 Jun 2018
 */
public interface AnnotationValueMatcher {

    static Matcher<OWLAnnotationValue> forAnyValue() {
        return value -> true;
    }

    static Matcher<OWLAnnotationValue> forAnyLiteral() {
        return OWLAnnotationValue::isLiteral;
    }

    static Matcher<OWLAnnotationValue> forAnyIri() {
        return OWLObject::isIRI;
    }

    static Matcher<OWLAnnotationValue> forAnyAnonymousIndividual() {
        return OWLObject::isIndividual;
    }

    static Matcher<OWLAnnotationValue> forIsNotInLexicalSpaceIfLiteral() {
        Matcher<OWLAnnotationValue> annotationValueMatcher = LiteralAnnotationValueMatcher.forIsNotInLexicalSpace();
        return value -> !value.isLiteral() || annotationValueMatcher.matches(value);
    }
}
