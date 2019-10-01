package edu.stanford.bmir.protege.web.server.persistence;

import com.mongodb.DBObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Oct 2016
 */
@RunWith(MockitoJUnitRunner.class)
public class OWLEntityConverter_TestCase {

    public static final String THE_IRI_STRING = "The IRI";

    private OWLEntityConverter converter;

    @Mock
    private OWLEntityProvider provider;

    @Mock
    private DBObject dbObject;

    private IRI iri = IRI.create(THE_IRI_STRING);

    @Mock
    private OWLClass cls;

    @Mock
    private OWLObjectProperty objectProperty;

    @Mock
    private OWLDataProperty dataProperty;

    @Mock
    private OWLAnnotationProperty annotationProperty;

    @Mock
    private OWLNamedIndividual namedIndividual;

    @Mock
    private OWLDatatype datatype;

    @Before
    public void setUp() {
        converter = new OWLEntityConverter(provider);
        when(provider.getOWLClass(iri)).thenReturn(cls);
        when(provider.getOWLObjectProperty(iri)).thenReturn(objectProperty);
        when(provider.getOWLDataProperty(iri)).thenReturn(dataProperty);
        when(provider.getOWLAnnotationProperty(iri)).thenReturn(annotationProperty);
        when(provider.getOWLNamedIndividual(iri)).thenReturn(namedIndividual);
        when(provider.getOWLDatatype(iri)).thenReturn(datatype);
        when(dbObject.get("iri")).thenReturn(iri);
    }

    @Test
    public void shouldConvertDBObjectToOWLClass() {
        decodeWithTypePropertyAndAssertEqualTo("Class", cls);
    }

    @Test
    public void shouldConvertDBObjectToOWLObjectProperty() {
        decodeWithTypePropertyAndAssertEqualTo("ObjectProperty", objectProperty);
    }

    @Test
    public void shouldConvertDBObjectToOWLDataProperty() {
        decodeWithTypePropertyAndAssertEqualTo("DataProperty", dataProperty);
    }

    @Test
    public void shouldConvertDBObjectToOWLAnnotationProperty() {
        decodeWithTypePropertyAndAssertEqualTo("AnnotationProperty", annotationProperty);
    }

    @Test
    public void shouldConvertDBObjectToOWLNamedIndividual() {
        decodeWithTypePropertyAndAssertEqualTo("NamedIndividual", namedIndividual);
    }

    @Test
    public void shouldConvertDBObjectToOWLDatatype() {
        decodeWithTypePropertyAndAssertEqualTo("Datatype", datatype);
    }

    @Test
    public void shouldReturnNullForMissingType() {
        decodeWithTypePropertyAndAssertEqualTo(null, null);
    }

    @Test
    public void shouldReturnNullForMissingIri() {
        when(dbObject.get("iri")).thenReturn(null);
        decodeWithTypePropertyAndAssertEqualTo("Class", null);
    }

    private void decodeWithTypePropertyAndAssertEqualTo(String type, Object expectedValue) {
        when(dbObject.get("type")).thenReturn(type);
        decodeObjectAndAssertEqualTo(expectedValue);
    }

    private void decodeObjectAndAssertEqualTo(Object expectedObject) {
        Object decodedObject = converter.decodeObject(dbObject, null);
        assertThat(decodedObject, is(equalTo(expectedObject)));
    }
}
