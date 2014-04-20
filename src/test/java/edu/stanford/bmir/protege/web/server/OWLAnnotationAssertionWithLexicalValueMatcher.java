package edu.stanford.bmir.protege.web.server;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.semanticweb.owlapi.model.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 19/04/2014
 */
public class OWLAnnotationAssertionWithLexicalValueMatcher extends TypeSafeMatcher<OWLAnnotationAssertionAxiom> {

    private String expectedValue;

    public OWLAnnotationAssertionWithLexicalValueMatcher(String expectedValue) {
        this.expectedValue = checkNotNull(expectedValue);
    }

    @Override
    protected boolean matchesSafely(OWLAnnotationAssertionAxiom item) {
        OWLAnnotationValue value = item.getValue();
        return value instanceof OWLLiteral && ((OWLLiteral) value).getLiteral().equals(expectedValue);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("annotation assertion with lexical value");
    }

    public static OWLAnnotationAssertionWithLexicalValueMatcher annotationAssertionWithLexicalValue(String lexicalValue) {
        return new OWLAnnotationAssertionWithLexicalValueMatcher(lexicalValue);
    }
}
