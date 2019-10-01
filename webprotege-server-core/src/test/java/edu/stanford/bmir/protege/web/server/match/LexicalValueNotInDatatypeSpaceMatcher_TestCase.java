package edu.stanford.bmir.protege.web.server.match;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16 Jun 2018
 */
@RunWith(MockitoJUnitRunner.class)
public class LexicalValueNotInDatatypeSpaceMatcher_TestCase {

    private LexicalValueNotInDatatypeSpaceMatcher matcher;

    @Mock
    private OWLLiteral literal;

    @Mock
    private OWLDatatype datatype;

    private String lexicalValue = "33";

    @Before
    public void setUp() {
        matcher = new LexicalValueNotInDatatypeSpaceMatcher();
        when(literal.getDatatype()).thenReturn(datatype);
        when(literal.getLiteral()).thenReturn(lexicalValue);
    }

    @Test
    public void shouldReturnFalseForNonOWL2Datatype() {
        when(datatype.isBuiltIn()).thenReturn(false);
        assertThat(matcher.matches(literal), is(false));
    }

    @Test
    public void shouldDelegateToOWL2DatatypeForBuiltInDatatypes() {
        when(datatype.isBuiltIn()).thenReturn(true);
        when(datatype.getBuiltInDatatype()).thenReturn(OWL2Datatype.XSD_INTEGER);
        matcher.matches(literal);
        verify(datatype, atLeastOnce()).getBuiltInDatatype();
    }

    @Test
    public void shouldMatchOWL2DatatypePatternAndReturnFalse() {
        when(datatype.isBuiltIn()).thenReturn(true);
        when(datatype.getBuiltInDatatype()).thenReturn(OWL2Datatype.XSD_INTEGER);
        assertThat(matcher.matches(literal), is(false));
    }

    @Test
    public void shouldNotMatchOWL2DatatypePatternAndReturnTrue() {
        when(datatype.isBuiltIn()).thenReturn(true);
        when(datatype.getBuiltInDatatype()).thenReturn(OWL2Datatype.XSD_DATE_TIME);
        assertThat(matcher.matches(literal), is(true));
    }
}
