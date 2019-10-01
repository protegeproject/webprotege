
package edu.stanford.bmir.protege.web.server.watches;

import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.shared.watches.Watch;
import edu.stanford.bmir.protege.web.shared.watches.WatchType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class Watch_TestCase {

    private Watch watch;

    @Mock
    private UserId userId;

    @Mock
    private OWLEntity entity;

    private WatchType type = WatchType.ENTITY;

    @Before
    public void setUp()
            throws Exception {
        watch = new Watch(userId, entity, type);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_userId_IsNull() {
        new Watch(null, entity, type);
    }

    @Test
    public void shouldReturnSupplied_userId() {
        assertThat(watch.getUserId(), is(this.userId));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_entity_IsNull() {
        new Watch(userId, null, type);
    }

    @Test
    public void shouldReturnSupplied_entity() {
        assertThat(watch.getEntity(), is(this.entity));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_type_IsNull() {
        new Watch(userId, entity, null);
    }

    @Test
    public void shouldReturnSupplied_type() {
        assertThat(watch.getType(), is(this.type));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(watch, is(watch));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(watch.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(watch, is(new Watch(userId, entity, type)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_userId() {
        assertThat(watch,
                   is(not(new Watch(mock(UserId.class), entity, type))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_entity() {
        assertThat(watch,
                   is(not(new Watch(userId,
                                    mock(OWLEntity.class),
                                    type))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_type() {
        assertThat(watch,
                   is(not(new Watch(userId,
                                    entity,
                                    WatchType.BRANCH))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(watch.hashCode(), is(new Watch(userId, entity, type).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(watch.toString(), startsWith("Watch"));
    }

}
