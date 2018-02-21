package edu.stanford.bmir.protege.web.server;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.semanticweb.owlapi.model.OWLLiteral;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 19/04/2014
 */
public class OWLLiteralWithLexicalValueMatcher extends TypeSafeMatcher<OWLLiteral> {

    private String expected;

    public OWLLiteralWithLexicalValueMatcher(String expectedLexicalValue) {
        this.expected = expectedLexicalValue;
    }

    @Override
    protected boolean matchesSafely(OWLLiteral literal) {
        return expected.equals(literal.getLiteral());
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("does not have lexical value");
    }


    public static OWLLiteralWithLexicalValueMatcher literalWithLexicalValue(String lexicalValue) {
        return new OWLLiteralWithLexicalValueMatcher(lexicalValue);
    }
}
