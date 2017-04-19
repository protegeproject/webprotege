
package edu.stanford.bmir.protege.web.server.watches;

import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class WatchRecord_TestCase {

    private WatchRecord watchRecord;

    @Mock
    private UserId userId;

    @Mock
    private OWLEntity entity;

    private WatchType type = WatchType.ENTITY;

    @Before
    public void setUp()
            throws Exception {
        watchRecord = new WatchRecord(userId, entity, type);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_userId_IsNull() {
        new WatchRecord(null, entity, type);
    }

    @Test
    public void shouldReturnSupplied_userId() {
        assertThat(watchRecord.getUserId(), is(this.userId));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_entity_IsNull() {
        new WatchRecord(userId, null, type);
    }

    @Test
    public void shouldReturnSupplied_entity() {
        assertThat(watchRecord.getEntity(), is(this.entity));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_type_IsNull() {
        new WatchRecord(userId, entity, null);
    }

    @Test
    public void shouldReturnSupplied_type() {
        assertThat(watchRecord.getType(), is(this.type));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(watchRecord, is(watchRecord));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(watchRecord.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(watchRecord, is(new WatchRecord(userId, entity, type)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_userId() {
        assertThat(watchRecord,
                   is(not(new WatchRecord(mock(UserId.class), entity, type))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_entity() {
        assertThat(watchRecord,
                   is(not(new WatchRecord(userId,
                                          mock(OWLEntity.class),
                                          type))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_type() {
        assertThat(watchRecord,
                   is(not(new WatchRecord(userId,
                                          entity,
                                          WatchType.BRANCH))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(watchRecord.hashCode(), is(new WatchRecord(userId, entity, type).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(watchRecord.toString(), startsWith("WatchRecord"));
    }

}
