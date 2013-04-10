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

    

    public VisualNamedClass translateToVisual(OWLClass ent) {
        return new VisualNamedClass(ent, project.getRenderingManager().getBrowserText(ent));
    }

    public VisualNamedIndividual translateToVisual(OWLNamedIndividual ent) {
        return new VisualNamedIndividual(ent, project.getRenderingManager().getBrowserText(ent));
    }


    public VisualObjectProperty translateToVisual(OWLObjectProperty ent) {
        return new VisualObjectProperty(ent, project.getRenderingManager().getBrowserText(ent));
    }


}
