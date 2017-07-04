
package edu.stanford.bmir.protege.web.shared.form.data;

import java.util.*;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static cern.clhep.Units.m;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class FormDataList_TestCase {

    private FormDataList formDataList;

    @Mock
    private FormDataValue val;

    private List<FormDataValue> list;

    @Before
    public void setUp() {
        list = Collections.singletonList(val);
        formDataList = new FormDataList(list);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_value_IsNull() {
        new FormDataList((List) null);
    }

    @Test
    public void shouldReturnSupplied_list() {
        assertThat(formDataList.getList(), Matchers.is(this.list));
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(formDataList, Matchers.is(formDataList));
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    public void shouldNotBeEqualToNull() {
        assertThat(formDataList.equals(null), Matchers.is(false));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(formDataList, Matchers.is(new FormDataList(list)));
    }

    @Test
    public void shouldNotBeEqualToOtherThatHasDifferent_value() {
        assertThat(formDataList, Matchers.is(Matchers.not(new FormDataList(Arrays.asList(mock(FormDataValue.class))))));
    }


    @Test
    public void shouldImplementToString() {
        assertThat(formDataList.toString(), Matchers.startsWith("FormDataList"));
    }

    @Test
    public void should_asList() {
        assertThat(formDataList.asList(), Matchers.is(list));
    }

    @Test
    public void shouldReturn_false_For_isEmpty() {
        assertThat(formDataList.isEmpty(), Matchers.is(false));
    }

    @Test
    public void should_getFirst() {
        assertThat(formDataList.getFirst(), Matchers.is(Optional.of(val)));
    }

    @Test
    public void shouldReturn_false_For_isObject() {
        assertThat(formDataList.isObject(), Matchers.is(false));
    }

}
