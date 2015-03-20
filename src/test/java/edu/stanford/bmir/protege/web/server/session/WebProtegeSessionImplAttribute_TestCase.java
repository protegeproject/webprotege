package edu.stanford.bmir.protege.web.server.session;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class WebProtegeSessionImplAttribute_TestCase {


    public static final String ATTRIBUTE_NAME = "ATTRIBUTE_NAME";

    private WebProtegeSessionAttribute attribute;

    private WebProtegeSessionAttribute otherAttribute;


    @Before
    public void setUp() throws Exception {
        attribute = new WebProtegeSessionAttribute(ATTRIBUTE_NAME);
        otherAttribute = new WebProtegeSessionAttribute(ATTRIBUTE_NAME);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerException() {
        new WebProtegeSessionAttribute(null);
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(attribute, is(equalTo(attribute)));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(attribute, is(not(equalTo(null))));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(attribute, is(equalTo(otherAttribute)));
    }

    @Test
    public void shouldHaveSameHashCodeAsOther() {
        assertThat(attribute.hashCode(), is(otherAttribute.hashCode()));
    }

    @Test
    public void shouldGenerateToString() {
        assertThat(attribute.toString(), startsWith("WebProtegeSessionAttribute"));
    }

    @Test
    public void shouldReturnSuppliedName() {
        assertThat(attribute.getAttributeName(), is(ATTRIBUTE_NAME));
    }
}