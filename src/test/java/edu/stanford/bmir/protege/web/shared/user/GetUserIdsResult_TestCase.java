package edu.stanford.bmir.protege.web.shared.user;

import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/01/15
 */
@RunWith(MockitoJUnitRunner.class)
public class GetUserIdsResult_TestCase {


    private GetUserIdsResult getUserIdsResult;

    private GetUserIdsResult otherGetUserIdsResult;

    @Mock
    private ImmutableList<UserId> userIds;

    @Before
    public void setUp() throws Exception {
        getUserIdsResult = new GetUserIdsResult(userIds);
        otherGetUserIdsResult = new GetUserIdsResult(userIds);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerException() {
        new GetUserIdsResult(null);
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(getUserIdsResult, is(equalTo(getUserIdsResult)));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(getUserIdsResult, is(not(equalTo(null))));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(getUserIdsResult, is(equalTo(otherGetUserIdsResult)));
    }

    @Test
    public void shouldHaveSameHashCodeAsOther() {
        assertThat(getUserIdsResult.hashCode(), is(otherGetUserIdsResult.hashCode()));
    }

    @Test
    public void shouldGenerateToString() {
        assertThat(getUserIdsResult.toString(), startsWith("GetUserIdsResult"));
    }
}