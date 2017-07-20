package edu.stanford.bmir.protege.web.shared.form.data;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.vocab.XSDVocabulary;

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
public class FormDataPrimitive_Number_TestCase {


    private static final double NUMBER = 33.3;

    private FormDataPrimitive primitive;

    @Before
    public void setUp() {
        primitive = FormDataPrimitive.get(NUMBER);
    }

    @Test
    public void shouldReturnValueFor_getValue() {
        assertThat(primitive.getValue(), is(NUMBER));
    }


    @Test(expected = RuntimeException.class)
    public void shouldThrowRuntimeExceptionOn_getValueAsString() {
        primitive.getValueAsString();
    }


    @Test
    public void shouldReturnValueFor_getValueAsDouble() {
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
    public void shouldReturnTrueFor_isNumber() {
        assertThat(primitive.isNumber(), is(true));
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
    public void shouldReturnDecimalLiteralFor_toOWLObject() {
        assertThat(primitive.toOWLObject(), is(instanceOf(OWLLiteral.class)));
        OWLLiteral literal = (OWLLiteral) primitive.toOWLObject();
        assertThat(literal.getLiteral(), is("33.3"));
        assertThat(literal.getDatatype().getIRI(), is(XSDVocabulary.DECIMAL.getIRI()));
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    public void shouldReturnDecimalLiteralFor_asOWLLiteral() {
        assertThat(primitive.asLiteral().isPresent(), is(true));
        OWLLiteral literal = primitive.asLiteral().get();
        assertThat(literal.getLiteral(), is("33.3"));
        assertThat(literal.getDatatype().getIRI(), is(XSDVocabulary.DECIMAL.getIRI()));
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
        assertThat(primitive, is(FormDataPrimitive.get(NUMBER)));
    }

    @Test
    public void shouldNotBeEqualToOther() {
        assertThat(primitive, is(not(FormDataPrimitive.get(44.4))));
    }

    @Test
    public void shouldHaveSameHashCodeAsOther() {
        assertThat(primitive.hashCode(), is(FormDataPrimitive.get(NUMBER).hashCode()));
    }
}
