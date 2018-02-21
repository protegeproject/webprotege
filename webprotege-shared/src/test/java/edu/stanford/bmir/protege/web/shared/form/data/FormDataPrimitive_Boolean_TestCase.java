package edu.stanford.bmir.protege.web.shared.form.data;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.OWLLiteral;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Jul 2017
 */
public class FormDataPrimitive_Boolean_TestCase {

    private FormDataPrimitive primitive;

    @Before
    public void setUp() {
        primitive = FormDataPrimitive.get(true);
    }

    @Test
    public void shouldReturnBooleanValue() {
        assertThat(primitive.getValue(), is(true));
    }

    @Test
    public void shouldReturnBooleanValueAsBoolean() {
        assertThat(primitive.getValueAsBoolean(), is(true));
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowRuntimeExceptionOn_getValueAsDouble() {
        primitive.getValueAsDouble();
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowRuntimeExceptionOn_getValueAsString() {
        primitive.getValueAsString();
    }

    @Test
    public void shouldReturnTrueFor_isBoolean() {
        assertThat(primitive.isBoolean(), is(true));
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
    public void shouldReturnBooleanLiteralFor_toOWLObject() {
        assertThat(primitive.toOWLObject(), is(instanceOf(OWLLiteral.class)));
        OWLLiteral literal = (OWLLiteral) primitive.toOWLObject();
        assertThat(literal.getLiteral(), is("true"));
        assertThat(literal.getDatatype().isBoolean(), is(true));
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    public void shouldReturnBooleanLiteralFor_asOWLLiteral() {
        assertThat(primitive.asLiteral().isPresent(), is(true));
        OWLLiteral literal = primitive.asLiteral().get();
        assertThat(literal.getLiteral(), is("true"));
        assertThat(literal.getDatatype().isBoolean(), is(true));
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
        assertThat(primitive, is(FormDataPrimitive.get(true)));
    }

    @Test
    public void shouldNotBeEqualToOther() {
        assertThat(primitive, is(not(FormDataPrimitive.get(false))));
    }

    @Test
    public void shouldHaveSameHashCodeAsOther() {
        assertThat(primitive.hashCode(), is(FormDataPrimitive.get(true).hashCode()));
    }
}
