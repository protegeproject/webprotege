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
public class OWLEntityWriteConverter_TestCase {

    private OWLEntityWriteConverter converter;

    @Mock
    private OWLEntity entity;

    private IRI iri = IRI.create("http://the.entity.iri");

    @Before
    public void setUp() throws Exception {
        converter = new OWLEntityWriteConverter();
        when(entity.getIRI()).thenReturn(iri);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldWriteOWLClass() {
        when(entity.getEntityType()).thenReturn((EntityType) EntityType.CLASS);
        DBObject dbObject = converter.convert(entity);
        assertThat(dbObject.get("type"), is("Class"));
        assertThat(dbObject.get("iri"), is(iri.toString()));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldWriteOWLObjectProperty() {
        when(entity.getEntityType()).thenReturn((EntityType) EntityType.OBJECT_PROPERTY);
        DBObject dbObject = converter.convert(entity);
        assertThat(dbObject.get("type"), is("ObjectProperty"));
        assertThat(dbObject.get("iri"), is(iri.toString()));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldWriteOWLDataProperty() {
        when(entity.getEntityType()).thenReturn((EntityType) EntityType.DATA_PROPERTY);
        DBObject dbObject = converter.convert(entity);
        assertThat(dbObject.get("type"), is("DataProperty"));
        assertThat(dbObject.get("iri"), is(iri.toString()));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldWriteOWLAnnotationProperty() {
        when(entity.getEntityType()).thenReturn((EntityType) EntityType.ANNOTATION_PROPERTY);
        DBObject dbObject = converter.convert(entity);
        assertThat(dbObject.get("type"), is("AnnotationProperty"));
        assertThat(dbObject.get("iri"), is(iri.toString()));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldWriteOWLNamedIndividual() {
        when(entity.getEntityType()).thenReturn((EntityType) EntityType.NAMED_INDIVIDUAL);
        DBObject dbObject = converter.convert(entity);
        assertThat(dbObject.get("type"), is("NamedIndividual"));
        assertThat(dbObject.get("iri"), is(iri.toString()));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldWriteOWLDatatype() {
        when(entity.getEntityType()).thenReturn((EntityType) EntityType.DATATYPE);
        DBObject dbObject = converter.convert(entity);
        assertThat(dbObject.get("type"), is("Datatype"));
        assertThat(dbObject.get("iri"), is(iri.toString()));
    }
}
