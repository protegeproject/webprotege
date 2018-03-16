
package edu.stanford.bmir.protege.web.shared.tag;

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

    private String tagLabel = "The tagLabel";

    @Mock
    private Color color;

    @Mock
    private Color backgroundColor;

    @Before
    public void setUp() {
        tag = new Tag(tagId, tagLabel, color, backgroundColor);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_tagId_IsNull() {
        new Tag(null, tagLabel, color, backgroundColor);
    }

    @Test
    public void shouldReturnSupplied_tagId() {
        assertThat(tag.getTagId(), is(this.tagId));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_tagLabel_IsNull() {
        new Tag(tagId, null, color, backgroundColor);
    }

    @Test
    public void shouldReturnSupplied_tagLabel() {
        assertThat(tag.getTagLabel(), is(this.tagLabel));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_color_IsNull() {
        new Tag(tagId, tagLabel, null, backgroundColor);
    }

    @Test
    public void shouldReturnSupplied_color() {
        assertThat(tag.getColor(), is(this.color));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_backgroundColor_IsNull() {
        new Tag(tagId, tagLabel, color, null);
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
        assertThat(tag, is(new Tag(tagId, tagLabel, color, backgroundColor)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_tagId() {
        assertThat(tag, is(not(new Tag(mock(TagId.class), tagLabel, color, backgroundColor))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_tagLabel() {
        assertThat(tag, is(not(new Tag(tagId, "String-e1abea01-faad-4066-994b-b0361ad021a7", color, backgroundColor))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_color() {
        assertThat(tag, is(not(new Tag(tagId, tagLabel, mock(Color.class), backgroundColor))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_backgroundColor() {
        assertThat(tag, is(not(new Tag(tagId, tagLabel, color, mock(Color.class)))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(tag.hashCode(), is(new Tag(tagId, tagLabel, color, backgroundColor).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(tag.toString(), startsWith("Tag"));
    }

}
