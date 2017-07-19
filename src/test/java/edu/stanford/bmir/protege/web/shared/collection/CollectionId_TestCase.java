
package edu.stanford.bmir.protege.web.shared.collection;

import edu.stanford.bmir.protege.web.shared.collection.CollectionId;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(MockitoJUnitRunner.class)
public class CollectionId_TestCase {

    private static final String THE_ID = "12345678-1234-1234-1234-123456789abc";

    private CollectionId collectionId;

    @Before
    public void setUp() {
        collectionId = CollectionId.get(THE_ID);
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowRuntimeExceptionForInvalidId() {
        CollectionId.get("InvalidId");
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionForNullId() {
        CollectionId.get(null);
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
        assertThat(collectionId, is(CollectionId.get(THE_ID)));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(collectionId.hashCode(), is(CollectionId.get(THE_ID).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(collectionId.toString(), Matchers.startsWith("CollectionId"));
    }

    @Test
    public void should_getId() {
        assertThat(collectionId.getId(), is(THE_ID));
    }

}
