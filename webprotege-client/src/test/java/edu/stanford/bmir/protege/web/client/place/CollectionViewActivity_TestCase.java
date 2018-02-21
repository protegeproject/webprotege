
package edu.stanford.bmir.protege.web.client.place;

import edu.stanford.bmir.protege.web.client.collection.CollectionPresenter;
import edu.stanford.bmir.protege.web.shared.place.CollectionViewPlace;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(MockitoJUnitRunner.class)
public class CollectionViewActivity_TestCase {

    private CollectionViewActivity collectionViewActivity;
    @Mock
    private CollectionPresenter presenter;
    @Mock
    private CollectionViewPlace place;

    @Before
    public void setUp()
        throws Exception
    {
        collectionViewActivity = new CollectionViewActivity(presenter, place);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_presenter_IsNull() {
        new CollectionViewActivity(null, place);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_place_IsNull() {
        new CollectionViewActivity(presenter, null);
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(collectionViewActivity, is(collectionViewActivity));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(collectionViewActivity.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(collectionViewActivity, is(new CollectionViewActivity(presenter, place)));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(collectionViewActivity.hashCode(), is(new CollectionViewActivity(presenter, place).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(collectionViewActivity.toString(), Matchers.startsWith("CollectionViewActivity"));
    }
}
