package edu.stanford.bmir.protege.web.shared.auth;

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
 * 17/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class ChapResponse_TestCase {


    private ChapResponse response;

    private ChapResponse otherResponse;

    private byte [] bytes = {3, 3, 3, 3};

    @Before
    public void setUp() throws Exception {
        response = new ChapResponse(bytes);
        otherResponse = new ChapResponse(bytes);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerException() {
        new ChapResponse(null);
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(response, is(equalTo(response)));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(response, is(not(equalTo(null))));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(response, is(equalTo(otherResponse)));
    }

    @Test
    public void shouldHaveSameHashCodeAsOther() {
        assertThat(response.hashCode(), is(otherResponse.hashCode()));
    }

    @Test
    public void shouldGenerateToString() {
        assertThat(response.toString(), startsWith("ChapResponse"));
    }

    @Test
    public void shouldReturnSuppliedBytes() {
        assertThat(response.getBytes(), is(bytes));
        assertThat(response.getBytes() != bytes, is(true));
    }
}