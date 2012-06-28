package edu.stanford.bmir.protege.web.server.owlapi.metrics;

import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import org.semanticweb.owlapi.model.*;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/06/2012
 */
public class OWLAPIProjectMetrics {

    private OWLAPIProject project;
    
    
    private int classCount;



    
    
    public OWLAPIProjectMetrics(OWLAPIProject project) {
        this.project = project;
        OWLOntologyManager manager = project.getRootOntology().getOWLOntologyManager();
        manager.addOntologyChangeListener(new OWLOntologyChangeListener() {
            public void ontologiesChanged(List<? extends OWLOntologyChange> changes) throws OWLException {
                handleChanges(changes);
            }
        });
    }
    
    
    
    private void handleChanges(List<? extends OWLOntologyChange> changes) {
        
    }
    
    
    
    
    
    private class ChangeHandler implements OWLOntologyChangeVisitor {

        public void visit(AddAxiom change) {
            
        }

        public void visit(RemoveAxiom change) {
        }

        public void visit(SetOntologyID change) {
        }

        public void visit(AddImport change) {
        }

        public void visit(RemoveImport change) {
        }

        public void visit(AddOntologyAnnotation change) {
        }

        public void visit(RemoveOntologyAnnotation change) {
        }
    }
}
