
package edu.stanford.bmir.protege.web.shared.tag;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.renderer.Color;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class Tag_TestCase {

    private Tag tag;

    @Mock
    private TagId tagId;

    @Mock
    private ProjectId projectId;

    private String tagLabel = "The label";

    private String description = "The description";

    @Mock
    private Color color;

    @Mock
    private Color backgroundColor;

    @Before
    public void setUp() {
        tag = new Tag(tagId, projectId, tagLabel, description, color, backgroundColor);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_tagId_IsNull() {
        new Tag(null, projectId, tagLabel, description, color, backgroundColor);
    }

    @Test
    public void shouldReturnSupplied_tagId() {
        assertThat(tag.getTagId(), is(this.tagId));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        new Tag(tagId, null, tagLabel, description, color, backgroundColor);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        assertThat(tag.getProjectId(), is(this.projectId));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_tagLabel_IsNull() {
        new Tag(tagId, projectId, null, description, color, backgroundColor);
    }

    @Test
    public void shouldReturnSupplied_tagLabel() {
        assertThat(tag.getTagLabel(), is(this.tagLabel));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_description_IsNull() {
        new Tag(tagId, projectId, tagLabel, null, color, backgroundColor);
    }

    @Test
    public void shouldReturnSupplied_description() {
        assertThat(tag.getDescription(), is(this.description));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_color_IsNull() {
        new Tag(tagId, projectId, tagLabel, description, null, backgroundColor);
    }

    @Test
    public void shouldReturnSupplied_color() {
        assertThat(tag.getColor(), is(this.color));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_backgroundColor_IsNull() {
        new Tag(tagId, projectId, tagLabel, description, color, null);
    }

    @Test
    public void shouldReturnSupplied_backgroundColor() {
        assertThat(tag.getBackgroundColor(), is(this.backgroundColor));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(tag, is(tag));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(tag.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(tag, is(new Tag(tagId, projectId, tagLabel, description, color, backgroundColor)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_tagId() {
        assertThat(tag, is(not(new Tag(mock(TagId.class), projectId, tagLabel, description, color, backgroundColor))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(tag, is(not(new Tag(tagId, mock(ProjectId.class), tagLabel, description, color, backgroundColor))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_tagLabel() {
        assertThat(tag, is(not(new Tag(tagId, projectId, "String-c3bf64b2-a1f6-460b-9cbb-deb440eaefe3", description, color, backgroundColor))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_description() {
        assertThat(tag, is(not(new Tag(tagId, projectId, tagLabel, "String-5a51416b-66bd-4f1c-bbbc-ca20837cd576", color, backgroundColor))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_color() {
        assertThat(tag, is(not(new Tag(tagId, projectId, tagLabel, description, mock(Color.class), backgroundColor))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_backgroundColor() {
        assertThat(tag, is(not(new Tag(tagId, projectId, tagLabel, description, color, mock(Color.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(tag.hashCode(), is(new Tag(tagId, projectId, tagLabel, description, color, backgroundColor).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(tag.toString(), startsWith("Tag"));
    }

}
