package edu.stanford.bmir.protege.web.server.persistence;

import com.mongodb.DBObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27 Jul 16
 */
@RunWith(MockitoJUnitRunner.class)
public class OWLEntityReadConverter_TestCase {

    private OWLEntityReadConverter converter;

    private IRI iri = IRI.create("http://the.entity.iri");

    @Mock
    private DBObject dbObject;

    @Before
    public void setUp() throws Exception {
        converter = new OWLEntityReadConverter();
        when(dbObject.get("iri")).thenReturn(iri.toString());
    }

    @Test
    public void shouldReadOWLClass() {
        when(dbObject.get("type")).thenReturn(EntityType.CLASS.getName());
        OWLEntity entity = converter.convert(dbObject);
        assertThat(entity.isOWLClass(), is(true));
        assertThat(entity.getIRI(), is(iri));
    }

    @Test
    public void shouldReadOWLObjectProperty() {
        when(dbObject.get("type")).thenReturn(EntityType.OBJECT_PROPERTY.getName());
        OWLEntity entity = converter.convert(dbObject);
        assertThat(entity.isOWLObjectProperty(), is(true));
        assertThat(entity.getIRI(), is(iri));
    }

    @Test
    public void shouldReadOWLDataProperty() {
        when(dbObject.get("type")).thenReturn(EntityType.DATA_PROPERTY.getName());
        OWLEntity entity = converter.convert(dbObject);
        assertThat(entity.isOWLDataProperty(), is(true));
        assertThat(entity.getIRI(), is(iri));
    }

    @Test
    public void shouldReadOWLAnnotationProperty() {
        when(dbObject.get("type")).thenReturn(EntityType.ANNOTATION_PROPERTY.getName());
        OWLEntity entity = converter.convert(dbObject);
        assertThat(entity.isOWLAnnotationProperty(), is(true));
        assertThat(entity.getIRI(), is(iri));
    }

    @Test
    public void shouldReadOWLNamedIndividual() {
        when(dbObject.get("type")).thenReturn(EntityType.NAMED_INDIVIDUAL.getName());
        OWLEntity entity = converter.convert(dbObject);
        assertThat(entity.isOWLNamedIndividual(), is(true));
        assertThat(entity.getIRI(), is(iri));
    }

    @Test
    public void shouldReadOWLDatatype() {
        when(dbObject.get("type")).thenReturn(EntityType.DATATYPE.getName());
        OWLEntity entity = converter.convert(dbObject);
        assertThat(entity.isOWLDatatype(), is(true));
        assertThat(entity.getIRI(), is(iri));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnMissingIRIPropertyValue() {
        when(dbObject.get("type")).thenReturn(EntityType.CLASS.getName());
        when(dbObject.get("iri")).thenReturn(null);
        converter.convert(dbObject);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnMissingTypePropertyValue() {
        converter.convert(dbObject);
    }
}
