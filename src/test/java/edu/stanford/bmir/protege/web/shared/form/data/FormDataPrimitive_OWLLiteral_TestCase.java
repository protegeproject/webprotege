package edu.stanford.bmir.protege.web.shared.form.data;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLLiteral;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Jul 2017
 */
@RunWith(MockitoJUnitRunner.class)
public class FormDataPrimitive_OWLLiteral_TestCase {


    private FormDataPrimitive primitive;

    @Mock
    private OWLLiteral literal;


    @Before
    public void setUp() {
        primitive = FormDataPrimitive.get(literal);
    }

    @Test
    public void shouldReturnLiteralValue() {
        assertThat(primitive.getValue(), is(literal));
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowRuntimeExceptionFpr_getValueAsString() {
        primitive.getValueAsString();
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowRuntimeExceptionOn_getValueAsDouble() {
        primitive.getValueAsDouble();
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowRuntimeExceptionOn_getValueAsBoolean() {
        primitive.getValueAsBoolean();
    }

    @Test
    public void shouldReturnFalseFor_isBoolean() {
        assertThat(primitive.isBoolean(), is(false));
    }

    @Test
    public void shouldReturnFalseFor_isNumber() {
        assertThat(primitive.isNumber(), is(false));
    }

    @Test
    public void shouldReturnFalseFor_isString() {
        assertThat(primitive.isString(), is(false));
    }

    @Test
    public void shouldReturnSingletonList() {
        assertThat(primitive.asList(), hasItem(primitive));
    }

    @Test
    public void shouldReturnFalseFor_isObject() {
        assertThat(primitive.isObject(), is(false));
    }

    @Test
    public void shouldReturnLiteralFor_toOWLObject() {
        assertThat(primitive.toOWLObject(), is(literal));
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    public void shouldReturnLiteralFor_asOWLLiteral() {
        assertThat(primitive.asLiteral().isPresent(), is(true));
        OWLLiteral lit = primitive.asLiteral().get();
        assertThat(lit, is(literal));
    }

    @Test
    public void shouldReturnEmptyFor_asOWLEntity() {
        assertThat(primitive.asOWLEntity().isPresent(), is(false));
    }

    @Test
    public void shouldReturnEmptyFor_asIri() {
        assertThat(primitive.asIRI().isPresent(), is(false));
    }


    @Test
    public void shouldBeEqualToOther() {
        assertThat(primitive, is(FormDataPrimitive.get(literal)));
    }

    @Test
    public void shouldNotBeEqualToOther() {
        assertThat(primitive, is(not(FormDataPrimitive.get(mock(OWLLiteral.class)))));
    }

    @Test
    public void shouldHaveSameHashCodeAsOther() {
        assertThat(primitive.hashCode(), is(FormDataPrimitive.get(literal).hashCode()));
    }
}
