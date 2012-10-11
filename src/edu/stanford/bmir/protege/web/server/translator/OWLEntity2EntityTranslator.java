package edu.stanford.bmir.protege.web.server.translator;

import edu.stanford.bmir.protege.web.client.rpc.data.primitive.*;
import edu.stanford.bmir.protege.web.client.rpc.data.primitive.IRI;
import org.semanticweb.owlapi.model.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public class OWLEntity2EntityTranslator implements OWLEntityVisitorEx<Entity> {

    public Entity visit(OWLClass cls) {
        return new NamedClass(IRI.create(cls.getIRI().toString()));
    }

    public Entity visit(OWLObjectProperty property) {
        return new ObjectProperty(IRI.create(property.getIRI().toString()));
    }

    public Entity visit(OWLDataProperty property) {
        return new DataProperty(IRI.create(property.getIRI().toString()));
    }

    public Entity visit(OWLNamedIndividual individual) {
        return new NamedIndividual(IRI.create(individual.getIRI().toString()));
    }

    public Entity visit(OWLDatatype datatype) {
        return new Datatype(IRI.create(datatype.getIRI().toString()));
    }

    public Entity visit(OWLAnnotationProperty property) {
        return new AnnotationProperty(IRI.create(property.getIRI().toString()));
    }
}
