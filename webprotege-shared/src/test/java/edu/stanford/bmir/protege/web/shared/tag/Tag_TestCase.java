
package edu.stanford.bmir.protege.web.shared.tag;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.match.criteria.RootCriteria;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.color.Color;
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
    
    private ImmutableList<RootCriteria> criteria;

    @Mock
    private RootCriteria rootCriteria;
    
    @Before
    public void setUp() {
        criteria = ImmutableList.of(rootCriteria);
        tag = Tag.get(tagId, projectId, tagLabel, description, color, backgroundColor, criteria);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_tagId_IsNull() {
        Tag.get(null, projectId, tagLabel, description, color, backgroundColor, criteria);
    }

    @Test
    public void shouldReturnSupplied_tagId() {
        assertThat(tag.getTagId(), is(this.tagId));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_projectId_IsNull() {
        Tag.get(tagId, null, tagLabel, description, color, backgroundColor, criteria);
    }

    @Test
    public void shouldReturnSupplied_projectId() {
        assertThat(tag.getProjectId(), is(this.projectId));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_tagLabel_IsNull() {
        Tag.get(tagId, projectId, null, description, color, backgroundColor, criteria);
    }

    @Test
    public void shouldReturnSupplied_tagLabel() {
        assertThat(tag.getLabel(), is(this.tagLabel));
    }
    @Test
    public void shouldReturnSupplied_description() {
        assertThat(tag.getDescription(), is(this.description));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_color_IsNull() {
        Tag.get(tagId, projectId, tagLabel, description, null, backgroundColor, criteria);
    }

    @Test
    public void shouldReturnSupplied_color() {
        assertThat(tag.getColor(), is(this.color));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_backgroundColor_IsNull() {
        Tag.get(tagId, projectId, tagLabel, description, color, null, criteria);
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
        assertThat(tag, is(Tag.get(tagId, projectId, tagLabel, description, color, backgroundColor, criteria)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_tagId() {
        assertThat(tag, is(not(Tag.get(mock(TagId.class), projectId, tagLabel, description, color, backgroundColor, criteria))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_projectId() {
        assertThat(tag, is(not(Tag.get(tagId, mock(ProjectId.class), tagLabel, description, color, backgroundColor, criteria))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_tagLabel() {
        assertThat(tag, is(not(Tag.get(tagId, projectId, "String-c3bf64b2-a1f6-460b-9cbb-deb440eaefe3", description, color, backgroundColor, criteria))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_description() {
        assertThat(tag, is(not(Tag.get(tagId, projectId, tagLabel, "String-5a51416b-66bd-4f1c-bbbc-ca20837cd576", color, backgroundColor, criteria))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_color() {
        assertThat(tag, is(not(Tag.get(tagId, projectId, tagLabel, description, mock(Color.class), backgroundColor, criteria))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_backgroundColor() {
        assertThat(tag, is(not(Tag.get(tagId, projectId, tagLabel, description, color, mock(Color.class), criteria))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(tag.hashCode(), is(Tag.get(tagId, projectId, tagLabel, description, color, backgroundColor, criteria).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(tag.toString(), startsWith("Tag"));
    }

}
