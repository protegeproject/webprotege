package edu.stanford.bmir.protege.web.shared.form.data;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLLiteral;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Jul 2017
 */
public class FormDataPrimitive_String_TestCase {

    private static final String THE_STRING = "TheString";

    private static final String OTHER_STRING = "OtherString";

    private FormDataPrimitive primitive;

    @Before
    public void setUp() {
        primitive = FormDataPrimitive.get(THE_STRING);
    }

    @Test
    public void shouldReturnBooleanValue() {
        assertThat(primitive.getValue(), is(THE_STRING));
    }

    @Test
    public void shouldReturnStringValueAsString() {
        assertThat(primitive.getValueAsString(), is(THE_STRING));
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
    public void shouldReturnTrueFor_isString() {
        assertThat(primitive.isString(), is(true));
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
    public void shouldReturnStringLiteralFor_toOWLObject() {
        assertThat(primitive.toOWLObject(), is(instanceOf(OWLLiteral.class)));
        OWLLiteral literal = (OWLLiteral) primitive.toOWLObject();
        assertThat(literal.getLiteral(), is(THE_STRING));
        assertThat(literal.getDatatype().isString(), is(true));
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    public void shouldReturnStringLiteralFor_asOWLLiteral() {
        assertThat(primitive.asLiteral().isPresent(), is(true));
        OWLLiteral literal = primitive.asLiteral().get();
        assertThat(literal.getLiteral(), is(THE_STRING));
        assertThat(literal.getDatatype().isString(), is(true));
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
        assertThat(primitive, is(FormDataPrimitive.get(THE_STRING)));
    }

    @Test
    public void shouldNotBeEqualToOther() {
        assertThat(primitive, is(not(FormDataPrimitive.get(OTHER_STRING))));
    }

    @Test
    public void shouldHaveSameHashCodeAsOther() {
        assertThat(primitive.hashCode(), is(FormDataPrimitive.get(THE_STRING).hashCode()));
    }
}
