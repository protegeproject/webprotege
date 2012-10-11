package edu.stanford.bmir.protege.web.server.translator;

import edu.stanford.bmir.protege.web.client.rpc.data.primitive.*;
import edu.stanford.bmir.protege.web.client.rpc.data.primitive.IRI;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import org.semanticweb.owlapi.model.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public class ObjectTranslator {

    private OWLAPIProject project;
    
    private OWLDataFactory dataFactory;

    public ObjectTranslator(OWLAPIProject project) {
        this.project = project;
        this.dataFactory = project.getDataFactory();
    }

    public ObjectTranslator(OWLDataFactory dataFactory) {
        this.dataFactory = dataFactory;
    }

    
    
    public OWLClass translate(NamedClass ent) {
        return dataFactory.getOWLClass(org.semanticweb.owlapi.model.IRI.create(ent.getIRI().getIRI()));
    }
    
    public NamedClass translate(OWLClass ent) {
        return new NamedClass(IRI.create(ent.getIRI().toString()));
    }
    
    public VisualNamedClass translateToVisual(OWLClass ent) {
        return new VisualNamedClass(translate(ent), project.getRenderingManager().getBrowserText(ent));
    }

    public VisualNamedIndividual translateToVisual(OWLNamedIndividual ent) {
        return new VisualNamedIndividual(translate(ent), project.getRenderingManager().getBrowserText(ent));
    }


    public OWLObjectProperty translate(ObjectProperty ent) {
        return dataFactory.getOWLObjectProperty(org.semanticweb.owlapi.model.IRI.create(ent.getIRI().getIRI()));
    }

    public ObjectProperty translate(OWLObjectProperty ent) {
        return new ObjectProperty(IRI.create(ent.getIRI().toString()));
    }
    
    public VisualObjectProperty translateToVisual(OWLObjectProperty ent) {
        return new VisualObjectProperty(translate(ent), project.getRenderingManager().getBrowserText(ent));
    }


    public OWLDataProperty translate(DataProperty ent) {
        return dataFactory.getOWLDataProperty(org.semanticweb.owlapi.model.IRI.create(ent.getIRI().getIRI()));
    }

    public DataProperty translate(OWLDataProperty ent) {
        return new DataProperty(IRI.create(ent.getIRI().toString()));
    }



    public OWLAnnotationProperty translate(AnnotationProperty ent) {
        return dataFactory.getOWLAnnotationProperty(org.semanticweb.owlapi.model.IRI.create(ent.getIRI().getIRI()));
    }

    public AnnotationProperty translate(OWLAnnotationProperty ent) {
        return new AnnotationProperty(IRI.create(ent.getIRI().toString()));
    }


    public OWLDatatype translate(Datatype ent) {
        return dataFactory.getOWLDatatype(org.semanticweb.owlapi.model.IRI.create(ent.getIRI().getIRI()));
    }

    public Datatype translate(OWLDatatype ent) {
        return new Datatype(IRI.create(ent.getIRI().toString()));
    }


    public OWLNamedIndividual translate(NamedIndividual ent) {
        return dataFactory.getOWLNamedIndividual(org.semanticweb.owlapi.model.IRI.create(ent.getIRI().getIRI()));
    }

    public NamedIndividual translate(OWLNamedIndividual ent) {
        return new NamedIndividual(IRI.create(ent.getIRI().toString()));
    }
    
}
