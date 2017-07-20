package edu.stanford.bmir.protege.web.shared.form.data;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.vocab.XSDVocabulary;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;


/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Jul 2017
 */
public class FormDataPrimitive_Iri_TestCase {


    private static final IRI iri = IRI.create("http://the.iri.com/iri");

    private FormDataPrimitive primitive;

    @Before
    public void setUp() {
        primitive = FormDataPrimitive.get(iri);
    }

    @Test
    public void shouldReturnValueFor_getValue() {
        assertThat(primitive.getValue(), is(iri));
    }


    @Test(expected = RuntimeException.class)
    public void shouldThrowRuntimeExceptionOn_getValueAsString() {
        primitive.getValueAsString();
    }


    @Test(expected = RuntimeException.class)
    public void shouldThrowRuntimeExceptionFor_getValueAsDouble() {
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
    public void shouldReturnIriFor_toOWLObject() {
        assertThat(primitive.toOWLObject(), is(instanceOf(IRI.class)));
        assertThat(primitive.toOWLObject(), is(iri));
    }

    @Test
    public void shouldReturnEmpty_asOWLLiteral() {
        assertThat(primitive.asLiteral().isPresent(), is(false));
    }

    @Test
    public void shouldReturnEmptyFor_asOWLEntity() {
        assertThat(primitive.asOWLEntity().isPresent(), is(false));
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    public void shouldReturnIriFor_asIri() {
        assertThat(primitive.asIRI().isPresent(), is(true));
        assertThat(primitive.asIRI().get(), is(iri));
    }


    @Test
    public void shouldBeEqualToOther() {
        assertThat(primitive, is(FormDataPrimitive.get(iri)));
    }

    @Test
    public void shouldNotBeEqualToOther() {
        assertThat(primitive, is(not(FormDataPrimitive.get(IRI.create("otherIRI")))));
    }

    @Test
    public void shouldHaveSameHashCodeAsOther() {
        assertThat(primitive.hashCode(), is(FormDataPrimitive.get(iri).hashCode()));
    }
}
