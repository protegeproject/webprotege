
package edu.stanford.bmir.protege.web.shared.ontology;

import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLOntologyID;

import static org.hamcrest.Matchers.*;

@RunWith(MockitoJUnitRunner.class)
public class OntologyIdData_TestCase {

    private OntologyIdData ontologyIdData;

    private OWLOntologyID ontologyID = new OWLOntologyID();

    private String browserText = "The browserText";

    @Before
    public void setUp()
        throws Exception
    {
        ontologyIdData = new OntologyIdData(ontologyID, browserText);
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_ontologyID_IsNull() {
        new OntologyIdData(null, browserText);
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_browserText_IsNull() {
        new OntologyIdData(ontologyID, null);
    }

    @Test
    public void shouldReturnSupplied_browserText() {
        MatcherAssert.assertThat(ontologyIdData.getBrowserText(), is(this.browserText));
    }

    @Test
    public void shouldBeEqualToSelf() {
        MatcherAssert.assertThat(ontologyIdData, is(ontologyIdData));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        MatcherAssert.assertThat(ontologyIdData.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        MatcherAssert.assertThat(ontologyIdData, is(new OntologyIdData(ontologyID, browserText)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_ontologyID() {
        MatcherAssert.assertThat(ontologyIdData, is(not(new OntologyIdData(new OWLOntologyID(), browserText))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_browserText() {
        MatcherAssert.assertThat(ontologyIdData, is(not(new OntologyIdData(ontologyID, "String-113be408-6933-4a00-833b-5df2376852f2"))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        MatcherAssert.assertThat(ontologyIdData.hashCode(), is(new OntologyIdData(ontologyID, browserText).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        MatcherAssert.assertThat(ontologyIdData.toString(), startsWith("OntologyIdData"));
    }

    @Test
    public void should_getUnquotedBrowserText() {
        MatcherAssert.assertThat(ontologyIdData.getUnquotedBrowserText(), is(browserText));
    }

}
