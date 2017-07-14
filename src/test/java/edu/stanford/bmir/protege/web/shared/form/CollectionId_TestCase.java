
package edu.stanford.bmir.protege.web.shared.form;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(MockitoJUnitRunner.class)
public class CollectionId_TestCase {

    private static final String MY_COLLECTION = "My Collection";

    private CollectionId collectionId;

    @Before
    public void setUp() {
        collectionId = CollectionId.get(MY_COLLECTION);
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(collectionId, is(collectionId));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(collectionId.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(collectionId, is(CollectionId.get(MY_COLLECTION)));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(collectionId.hashCode(), is(CollectionId.get(MY_COLLECTION).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(collectionId.toString(), Matchers.startsWith("CollectionId"));
    }

    @Test
    public void should_getId() {
        assertThat(collectionId.getId(), is(MY_COLLECTION));
    }

}
