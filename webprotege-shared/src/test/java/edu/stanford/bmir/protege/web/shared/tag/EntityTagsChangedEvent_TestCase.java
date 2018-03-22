
package edu.stanford.bmir.protege.web.shared.tag;

import java.util.Collection;
import java.util.Collections;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
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
public class EntityTagsChangedEvent_TestCase {

    private EntityTagsChangedEvent event;

    @Mock
    private ProjectId projectId;

    @Mock
    private OWLEntity entity;

    @Mock
    private Collection<Tag> tags;

    @Before
    public void setUp() {
        tags = Collections.singleton(mock(Tag.class));
        event = new EntityTagsChangedEvent(projectId, entity, tags);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        new EntityTagsChangedEvent(null, entity, tags);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        assertThat(event.getProjectId(), is(this.projectId));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_entity_IsNull() {
        new EntityTagsChangedEvent(projectId, null, tags);
    }

    @Test
    public void shouldReturnSupplied_entity() {
        assertThat(event.getEntity(), is(this.entity));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_tags_IsNull() {
        new EntityTagsChangedEvent(projectId, entity, null);
    }

    @Test
    public void shouldReturnSupplied_tags() {
        assertThat(event.getTags(), is(this.tags));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(event, is(event));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(event.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(event, is(new EntityTagsChangedEvent(projectId, entity, tags)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(event, is(not(new EntityTagsChangedEvent(mock(ProjectId.class), entity, tags))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_entity() {
        assertThat(event, is(not(new EntityTagsChangedEvent(projectId, mock(OWLEntity.class), tags))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_tags() {
        assertThat(event, is(not(new EntityTagsChangedEvent(projectId, entity, Collections.singleton(mock(Tag.class))))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(event.hashCode(), is(new EntityTagsChangedEvent(projectId, entity, tags).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(event.toString(), startsWith("EntityTagsChangedEvent"));
    }
}
