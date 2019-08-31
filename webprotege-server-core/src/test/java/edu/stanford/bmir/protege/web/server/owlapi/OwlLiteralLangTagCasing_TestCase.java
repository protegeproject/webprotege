package edu.stanford.bmir.protege.web.server.owlapi;

import org.junit.Before;
import org.junit.Test;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-31
 *
 * Test to check that OWL API lower cases lang tags when creating literals
 */
public class OwlLiteralLangTagCasing_TestCase {

    private OWLDataFactoryImpl dataFactory;

    @Before
    public void setUp() {
        dataFactory = new OWLDataFactoryImpl();
    }

    @Test
    public void shouldLowerCaseLangTag() {
        var literal = dataFactory.getOWLLiteral("lexicalvalue", "EN");
        assertThat(literal.getLang(), is("en"));
    }
}
