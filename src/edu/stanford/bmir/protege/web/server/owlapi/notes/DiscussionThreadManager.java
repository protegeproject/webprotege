package edu.stanford.bmir.protege.web.server.owlapi.notes;

import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.notes.api.DiscussionThread;
import edu.stanford.bmir.protege.web.server.owlapi.notes.api.DiscussionThreadNode;
import org.semanticweb.owlapi.model.OWLAnnotationSubject;
import org.semanticweb.owlapi.model.OWLAxiom;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 10/04/2012
 */
public class DiscussionThreadManager {

    private OWLAPIProject project;

    public DiscussionThreadManager(OWLAPIProject project) {
        this.project = project;
    }
    
    
    public DiscussionThread getDiscussionThread(OWLAnnotationSubject entity) {
        return new DiscussionThread();
    }

    public DiscussionThread getDiscussionThread(OWLAxiom axiom) {
        return new DiscussionThread();
    }
    
    public void addChild(DiscussionThread thread, DiscussionThreadNode parent, DiscussionThreadNode child) {
        
    }
    
    public void deleteChild(DiscussionThread thread, DiscussionThreadNode node, DiscussionThreadNode child) {

    }
}
