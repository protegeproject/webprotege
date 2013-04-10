package edu.stanford.bmir.protege.web.server.watches;

import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.shared.event.ClassFrameChangedEvent;
import edu.stanford.bmir.protege.web.shared.event.ClassFrameChangedEventHandler;
import org.semanticweb.owlapi.model.*;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/03/2013
 */
public class EntityFrameWatchNotificationGenerator implements WatchNotificationGenerator {

    private OWLOntologyChangeListener listener;

    private OWLAPIProject project;

    @Override
    public void initialize(OWLAPIProject project) {
        this.project = project;
        project.getEventManager().addHandler(ClassFrameChangedEvent.TYPE, new ClassFrameChangedEventHandler() {
            @Override
            public void classFrameChanged(ClassFrameChangedEvent event) {
                handleEntityFrameChanged(event.getEntity());
            }
        });
    }

    private void handleEntityFrameChanged(OWLEntity entity) {
        // Is this watched?
        entity.accept(new OWLEntityVisitor() {
            @Override
            public void visit(OWLClass cls) {
                for(OWLClass anc : project.getClassHierarchyProvider().getAncestors(cls)) {
                    // Is ancestor watched?
                }

            }

            @Override
            public void visit(OWLObjectProperty property) {
            }

            @Override
            public void visit(OWLDataProperty property) {
            }

            @Override
            public void visit(OWLNamedIndividual individual) {
            }

            @Override
            public void visit(OWLDatatype datatype) {
            }

            @Override
            public void visit(OWLAnnotationProperty property) {
            }
        });
    }

    @Override
    public void dispose() {

    }
}
