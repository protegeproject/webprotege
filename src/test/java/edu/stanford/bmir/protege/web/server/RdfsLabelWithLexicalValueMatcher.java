package edu.stanford.bmir.protege.web.server;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLLiteral;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 19/04/2014
 */
public class RdfsLabelWithLexicalValueMatcher extends TypeSafeMatcher<OWLAnnotationAssertionAxiom> {

    private String expectedValue;

    public RdfsLabelWithLexicalValueMatcher(String expectedValue) {
        this.expectedValue = expectedValue;
    }

    @Override
    protected boolean matchesSafely(OWLAnnotationAssertionAxiom item) {
        if(!item.getProperty().isLabel()) {
            return false;
        }
        OWLAnnotationValue value = item.getValue();
        return value instanceof OWLLiteral && ((OWLLiteral) value).getLiteral().equals(expectedValue);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("rdfs:Label annotation assertion with \"" + expectedValue + "\" value");
    }

    public static RdfsLabelWithLexicalValueMatcher rdfsLabelWithLexicalValue(String lexicalValue) {
        return new RdfsLabelWithLexicalValueMatcher(lexicalValue);
    }
}
