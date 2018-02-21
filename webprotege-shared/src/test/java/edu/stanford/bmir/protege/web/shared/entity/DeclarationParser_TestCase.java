package edu.stanford.bmir.protege.web.shared.entity;

import edu.stanford.bmir.protege.web.shared.DataFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14/12/15
 */
@RunWith(MockitoJUnitRunner.class)
public class DeclarationParser_TestCase {


    private IRI iri = IRI.create("http://ontology.com/ontologies/entities/myEntity");

    private DeclarationParser parser;

    @Before
    public void setUp() throws Exception {
        parser = new DeclarationParser(DataFactory.get());
    }

    @Test
    public void shouldReturnAbsent() {
        Optional<OWLEntity> entity = parser.parseEntity("Junk");
        assertThat(entity.isPresent(), is(false));
    }

    @Test
    public void shouldParseOWLClass() {
        String declaration = "Class(" + iri + ")";
        Optional<OWLEntity> entity = parser.parseEntity(declaration);
        assertThat(entity.isPresent(), is(true));
        assertThat(entity.get().isOWLClass(), is(true));
        assertThat(entity.get().getIRI(), is(iri));
    }
    
    @Test
    public void shouldParseOWLObjectProperty() {
        String declaration = "ObjectProperty(" + iri + ")";
        Optional<OWLEntity> entity = parser.parseEntity(declaration);
        assertThat(entity.isPresent(), is(true));
        assertThat(entity.get().isOWLObjectProperty(), is(true));
        assertThat(entity.get().getIRI(), is(iri));
    }
    
    @Test
    public void shouldParseOWLDataProperty() {
        String declaration = "DataProperty(" + iri + ")";
        Optional<OWLEntity> entity = parser.parseEntity(declaration);
        assertThat(entity.isPresent(), is(true));
        assertThat(entity.get().isOWLDataProperty(), is(true));
        assertThat(entity.get().getIRI(), is(iri));
    }
    
    @Test
    public void shouldParseOWLAnnotationProperty() {
        String declaration = "AnnotationProperty(" + iri + ")";
        Optional<OWLEntity> entity = parser.parseEntity(declaration);
        assertThat(entity.isPresent(), is(true));
        assertThat(entity.get().isOWLAnnotationProperty(), is(true));
        assertThat(entity.get().getIRI(), is(iri));
    }
    
    @Test
    public void shouldParseOWLNamedIndividual() {
        String declaration = "NamedIndividual(" + iri + ")";
        Optional<OWLEntity> entity = parser.parseEntity(declaration);
        assertThat(entity.isPresent(), is(true));
        assertThat(entity.get().isOWLNamedIndividual(), is(true));
        assertThat(entity.get().getIRI(), is(iri));
    }
    
    @Test
    public void shouldParseOWLDatatype() {
        String declaration = "Datatype(" + iri + ")";
        Optional<OWLEntity> entity = parser.parseEntity(declaration);
        assertThat(entity.isPresent(), is(true));
        assertThat(entity.get().isOWLDatatype(), is(true));
        assertThat(entity.get().getIRI(), is(iri));
    }
}
