package edu.stanford.bmir.protege.web.server.translator;

import edu.stanford.bmir.protege.web.client.rpc.data.primitive.*;
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

    
    
    public OWLClass translate(Cls ent) {
        return dataFactory.getOWLClass(IRI.create(ent.getIRI().getIRI()));
    }
    
    public Cls translate(OWLClass ent) {
        return new Cls(new WebProtegeIRI(ent.getIRI().toString()));
    }
    
    public VisualCls translateToVisual(OWLClass ent) {
        return new VisualCls(translate(ent), project.getRenderingManager().getBrowserText(ent));
    }

    public VisualNamedIndividual translateToVisual(OWLNamedIndividual ent) {
        return new VisualNamedIndividual(translate(ent), project.getRenderingManager().getBrowserText(ent));
    }


    public OWLObjectProperty translate(ObjectProperty ent) {
        return dataFactory.getOWLObjectProperty(IRI.create(ent.getIRI().getIRI()));
    }

    public ObjectProperty translate(OWLObjectProperty ent) {
        return new ObjectProperty(new WebProtegeIRI(ent.getIRI().toString()));
    }
    
    public VisualObjectProperty translateToVisual(OWLObjectProperty ent) {
        return new VisualObjectProperty(translate(ent), project.getRenderingManager().getBrowserText(ent));
    }


    public OWLDataProperty translate(DataProperty ent) {
        return dataFactory.getOWLDataProperty(IRI.create(ent.getIRI().getIRI()));
    }

    public DataProperty translate(OWLDataProperty ent) {
        return new DataProperty(new WebProtegeIRI(ent.getIRI().toString()));
    }



    public OWLAnnotationProperty translate(AnnotationProperty ent) {
        return dataFactory.getOWLAnnotationProperty(IRI.create(ent.getIRI().getIRI()));
    }

    public AnnotationProperty translate(OWLAnnotationProperty ent) {
        return new AnnotationProperty(new WebProtegeIRI(ent.getIRI().toString()));
    }


    public OWLDatatype translate(Datatype ent) {
        return dataFactory.getOWLDatatype(IRI.create(ent.getIRI().getIRI()));
    }

    public Datatype translate(OWLDatatype ent) {
        return new Datatype(new WebProtegeIRI(ent.getIRI().toString()));
    }


    public OWLNamedIndividual translate(NamedIndividual ent) {
        return dataFactory.getOWLNamedIndividual(IRI.create(ent.getIRI().getIRI()));
    }

    public NamedIndividual translate(OWLNamedIndividual ent) {
        return new NamedIndividual(new WebProtegeIRI(ent.getIRI().toString()));
    }
    
}
