
package edu.stanford.bmir.protege.web.shared.entity;

import java.util.HashSet;
import java.util.Set;

import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.semanticweb.owlapi.model.OWLEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.mock;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class DeleteEntitiesResult_TestCase {

    private DeleteEntitiesResult deleteEntitiesResult;

    @Mock
    private EventList<ProjectEvent<?>> events;

    private Set<OWLEntity> deletedEntities;

    @Before
    public void setUp() {
        deletedEntities = new HashSet<>();
        deletedEntities.add(mock(OWLEntity.class));
        deleteEntitiesResult = new DeleteEntitiesResult(events, deletedEntities);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_events_IsNull() {
        new DeleteEntitiesResult(null, deletedEntities);
    }

    @Test
    public void shouldReturnSupplied_events() {
        assertThat(deleteEntitiesResult.getEventList(), is(this.events));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_deletedEntities_IsNull() {
        new DeleteEntitiesResult(events, null);
    }

    @Test
    public void shouldReturnSupplied_deletedEntities() {
        assertThat(deleteEntitiesResult.getDeletedEntities(), is(this.deletedEntities));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(deleteEntitiesResult, is(deleteEntitiesResult));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(deleteEntitiesResult.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(deleteEntitiesResult, is(new DeleteEntitiesResult(events, deletedEntities)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_events() {
        assertThat(deleteEntitiesResult, is(not(new DeleteEntitiesResult(mock(EventList.class), deletedEntities))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_deletedEntities() {
        assertThat(deleteEntitiesResult, is(not(new DeleteEntitiesResult(events, new HashSet<>()))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(deleteEntitiesResult.hashCode(), is(new DeleteEntitiesResult(events, deletedEntities).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(deleteEntitiesResult.toString(), Matchers.startsWith("DeleteEntitiesResult"));
    }
}
