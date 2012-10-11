package edu.stanford.bmir.protege.web.server.translator;

import edu.stanford.bmir.protege.web.client.rpc.data.primitive.*;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public class Entity2OWLEntityTranslator implements EntityVisitor<OWLEntity, RuntimeException> {

    private OWLDataFactory dataFactory;

    public Entity2OWLEntityTranslator(OWLDataFactory dataFactory) {
        this.dataFactory = dataFactory;
    }

    private IRI toIRI(Entity entity) {
        return IRI.create(entity.getIRI().getIRI());
    }
    
    public OWLEntity visit(NamedClass cls) throws RuntimeException {
        return dataFactory.getOWLClass(toIRI(cls));
    }

    public OWLEntity visit(ObjectProperty property) throws RuntimeException {
        return dataFactory.getOWLObjectProperty(toIRI(property));
    }

    public OWLEntity visit(DataProperty property) throws RuntimeException {
        return dataFactory.getOWLDataProperty(toIRI(property));
    }

    public OWLEntity visit(AnnotationProperty property) throws RuntimeException {
        return dataFactory.getOWLAnnotationProperty(toIRI(property));
    }

    public OWLEntity visit(Datatype datatype) throws RuntimeException {
        return dataFactory.getOWLDatatype(toIRI(datatype));
    }

    public OWLEntity visit(NamedIndividual individual) throws RuntimeException {
        return dataFactory.getOWLNamedIndividual(toIRI(individual));
    }
}
