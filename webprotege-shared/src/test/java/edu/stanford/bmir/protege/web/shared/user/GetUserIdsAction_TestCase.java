package edu.stanford.bmir.protege.web.shared.user;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/01/15
 */
@RunWith(MockitoJUnitRunner.class)
public class GetUserIdsAction_TestCase {


    private GetUserIdsAction getUserIdsAction;

    private GetUserIdsAction otherGetUserIdsAction;


    @Before
    public void setUp() throws Exception {
        getUserIdsAction = new GetUserIdsAction();
        otherGetUserIdsAction = new GetUserIdsAction();
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(getUserIdsAction, is(equalTo(getUserIdsAction)));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(getUserIdsAction, is(not(equalTo(null))));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(getUserIdsAction, is(equalTo(otherGetUserIdsAction)));
    }

    @Test
    public void shouldHaveSameHashCodeAsOther() {
        assertThat(getUserIdsAction.hashCode(), is(otherGetUserIdsAction.hashCode()));
    }

    @Test
    public void shouldGenerateToString() {
        assertThat(getUserIdsAction.toString(), startsWith("GetUserIdsAction"));
    }
}