
package edu.stanford.bmir.protege.web.shared.color;

import edu.stanford.bmir.protege.web.shared.color.Color;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(MockitoJUnitRunner.class)
public class Color_TestCase {

    private Color color;

    private int red = 255;

    private int green = 10;

    private int blue = 120;

    @Before
    public void setUp() {
        color = Color.get(red, green, blue);
    }

    @Test
    public void shouldReturnSupplied_red() {
        assertThat(color.getRed(), is(this.red));
    }

    @Test
    public void shouldReturnSupplied_green() {
        assertThat(color.getGreen(), is(this.green));
    }

    @Test
    public void shouldReturnSupplied_blue() {
        assertThat(color.getBlue(), is(this.blue));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(color, is(color));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(color.equals(null), is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(color, is(Color.get(red, green, blue)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_red() {
        assertThat(color, is(not(Color.get(4, green, blue))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_green() {
        assertThat(color, is(not(Color.get(red, 5, blue))));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_blue() {
        assertThat(color, is(not(Color.get(red, green, 6))));
    }

    @Test
    public void shouldBeEqualToOtherHashCode() {
        assertThat(color.hashCode(), is(Color.get(red, green, blue).hashCode()));
    }

    @Test
    public void shouldImplementToString() {
        assertThat(color.toString(), Matchers.startsWith("Color"));
    }

    @Test
    public void should_getRGB() {
        assertThat(Color.getRGB(red, green, blue), is(color));
    }

    @Test
    public void should_getHex_From_String() {
        assertThat(Color.getHex("#ff0a78"), is(color));
    }

    @Test
    public void should_getHex_From_RGB_Strings() {
        assertThat(Color.getHex("ff", "0a", "78"), is(color));
    }

    @Test
    public void should_getHex() {
        assertThat(color.getHex(), is("#ff0a78"));
    }

    @Test
    public void should_getHSLColor() {
        Color c = Color.getHSL(0, 1, 0.5);
        assertThat(c.getRed(), is(255));
        assertThat(c.getGreen(), is(0));
        assertThat(c.getBlue(), is(0));
    }

}
