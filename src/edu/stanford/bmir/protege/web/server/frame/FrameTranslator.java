package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.shared.frame.Frame;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;

import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/01/2013
 */
public interface FrameTranslator<F extends Frame<S>, S> {

    F getFrame(S subject, OWLOntology rootOntology, OWLAPIProject project);

    Set<OWLAxiom> getAxioms(F frame);
}
