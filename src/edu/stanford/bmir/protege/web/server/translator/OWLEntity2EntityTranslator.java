package edu.stanford.bmir.protege.web.server.translator;

import edu.stanford.bmir.protege.web.client.rpc.data.primitive.*;
import edu.stanford.bmir.protege.web.client.rpc.data.primitive.WebProtegeIRI;
import org.semanticweb.owlapi.model.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public class OWLEntity2EntityTranslator implements OWLEntityVisitorEx<Entity> {

    public Entity visit(OWLClass cls) {
        return new Cls(new WebProtegeIRI(cls.getIRI().toString()));
    }

    public Entity visit(OWLObjectProperty property) {
        return new Cls(new WebProtegeIRI(property.getIRI().toString()));
    }

    public Entity visit(OWLDataProperty property) {
        return new DataProperty(new WebProtegeIRI(property.getIRI().toString()));
    }

    public Entity visit(OWLNamedIndividual individual) {
        return new NamedIndividual(new WebProtegeIRI(individual.getIRI().toString()));
    }

    public Entity visit(OWLDatatype datatype) {
        return new Datatype(new WebProtegeIRI(datatype.getIRI().toString()));
    }

    public Entity visit(OWLAnnotationProperty property) {
        return new AnnotationProperty(new WebProtegeIRI(property.getIRI().toString()));
    }
}
