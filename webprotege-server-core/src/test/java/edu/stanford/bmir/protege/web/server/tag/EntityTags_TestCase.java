
package edu.stanford.bmir.protege.web.server.tag;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.tag.TagId;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class EntityTags_TestCase {

    private EntityTags entityTags;

    @Mock
    private ProjectId projectId;

    @Mock
    private OWLEntity entity;

    private List<TagId> tags = Collections.singletonList(mock(TagId.class));

    @Before
    public void setUp() {
        entityTags = new EntityTags(projectId, entity, tags);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        new EntityTags(null, entity, tags);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        assertThat(entityTags.getProjectId(), is(this.projectId));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_entity_IsNull() {
        new EntityTags(projectId, null, tags);
    }

    @Test
    public void shouldReturnSupplied_entity() {
        assertThat(entityTags.getEntity(), is(this.entity));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_tags_IsNull() {
        new EntityTags(projectId, entity, null);
    }

    @Test
    public void shouldReturnSupplied_tags() {
        assertThat(entityTags.getTags(), is(this.tags));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(entityTags, is(entityTags));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(entityTags.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(entityTags, is(new EntityTags(projectId, entity, tags)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(entityTags, is(not(new EntityTags(mock(ProjectId.class), entity, tags))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_entity() {
        assertThat(entityTags, is(not(new EntityTags(projectId, mock(OWLEntity.class), tags))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_tags() {
        assertThat(entityTags, is(not(new EntityTags(projectId, entity, Collections.singletonList(mock(TagId.class))))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(entityTags.hashCode(), is(new EntityTags(projectId, entity, tags).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(entityTags.toString(), Matchers.startsWith("EntityTags"));
    }

}
