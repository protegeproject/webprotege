package edu.stanford.bmir.protege.web.shared.change;

import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class GetWatchedEntityChangesResult_TestCase {


    private GetWatchedEntityChangesResult result;

    private GetWatchedEntityChangesResult otherResult;

    @Mock
    private ImmutableList<ProjectChange> changes;


    @Before
    public void setUp() throws Exception {
        result = new GetWatchedEntityChangesResult(changes);
        otherResult = new GetWatchedEntityChangesResult(changes);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerException() {
        new GetWatchedEntityChangesResult(null);
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(result, is(equalTo(result)));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(result, is(not(equalTo(null))));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(result, is(equalTo(otherResult)));
    }

    @Test
    public void shouldHaveSameHashCodeAsOther() {
        assertThat(result.hashCode(), is(otherResult.hashCode()));
    }

    @Test
    public void shouldGenerateToString() {
        assertThat(result.toString(), startsWith("GetWatchedEntityChangesResult"));
    }

    @Test
    public void shouldReturnSuppliedChanges() {
        assertThat(result.getChanges(), is(changes));
    }
}