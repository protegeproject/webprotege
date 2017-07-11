
package edu.stanford.bmir.protege.web.shared.form.field;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(MockitoJUnitRunner.class)
public class TextFieldDescriptor_TestCase {

    private TextFieldDescriptor textFieldDescriptor;

    private String placeholder = "The placeholder";

    private StringType stringType = StringType.SIMPLE_STRING;

    private LineMode lineMode = LineMode.SINGLE_LINE;

    private String pattern = "The pattern";

    private String patternViolationErrorMessage = "The patternViolationErrorMessage";

    @Before
    public void setUp()
        throws Exception
    {
        textFieldDescriptor = new TextFieldDescriptor(placeholder, stringType, lineMode, pattern, patternViolationErrorMessage);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_placeholder_IsNull() {
        new TextFieldDescriptor(null, stringType, lineMode, pattern, patternViolationErrorMessage);
    }

    @Test
    public void shouldReturnSupplied_placeholder() {
        assertThat(textFieldDescriptor.getPlaceholder(), is(this.placeholder));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_stringType_IsNull() {
        new TextFieldDescriptor(placeholder, null, lineMode, pattern, patternViolationErrorMessage);
    }

    @Test
    public void shouldReturnSupplied_stringType() {
        assertThat(textFieldDescriptor.getStringType(), is(this.stringType));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_lineMode_IsNull() {
        new TextFieldDescriptor(placeholder, stringType, null, pattern, patternViolationErrorMessage);
    }

    @Test
    public void shouldReturnSupplied_lineMode() {
        assertThat(textFieldDescriptor.getLineMode(), is(this.lineMode));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_pattern_IsNull() {
        new TextFieldDescriptor(placeholder, stringType, lineMode, null, patternViolationErrorMessage);
    }

    @Test
    public void shouldReturnSupplied_pattern() {
        assertThat(textFieldDescriptor.getPattern(), is(this.pattern));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_patternViolationErrorMessage_IsNull() {
        new TextFieldDescriptor(placeholder, stringType, lineMode, pattern, null);
    }

    @Test
    public void shouldReturnSupplied_patternViolationErrorMessage() {
        assertThat(textFieldDescriptor.getPatternViolationErrorMessage(), is(this.patternViolationErrorMessage));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(textFieldDescriptor, is(textFieldDescriptor));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(textFieldDescriptor.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(textFieldDescriptor, is(new TextFieldDescriptor(placeholder, stringType, lineMode, pattern, patternViolationErrorMessage)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_placeholder() {
        assertThat(textFieldDescriptor, is(not(new TextFieldDescriptor("String-1a3f1304-8c2f-48b2-8c60-3407b27d579f", stringType, lineMode, pattern, patternViolationErrorMessage))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_stringType() {
        assertThat(textFieldDescriptor, is(not(new TextFieldDescriptor(placeholder, StringType.LANG_STRING, lineMode, pattern, patternViolationErrorMessage))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_lineMode() {
        assertThat(textFieldDescriptor, is(not(new TextFieldDescriptor(placeholder, stringType, LineMode.MULTI_LINE, pattern, patternViolationErrorMessage))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_pattern() {
        assertThat(textFieldDescriptor, is(not(new TextFieldDescriptor(placeholder, stringType, lineMode, "String-ac3398da-4b52-48b5-a858-1f8571134db7", patternViolationErrorMessage))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_patternViolationErrorMessage() {
        assertThat(textFieldDescriptor, is(not(new TextFieldDescriptor(placeholder, stringType, lineMode, pattern, "String-1abd718d-0eb4-4530-b465-02aed8dd66a2"))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(textFieldDescriptor.hashCode(), is(new TextFieldDescriptor(placeholder, stringType, lineMode, pattern, patternViolationErrorMessage).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(textFieldDescriptor.toString(), Matchers.startsWith("TextFieldDescriptor"));
    }

    @Test
    public void should_getAssociatedType() {
        assertThat(textFieldDescriptor.getAssociatedType(), is("Text"));
    }

    @Test
    public void should_getType() {
        assertThat(TextFieldDescriptor.getType(), is("Text"));
    }

}
