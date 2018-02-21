package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.shared.frame.Frame;
import org.semanticweb.owlapi.model.OWLAxiom;

import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/01/2013
 */
public interface FrameTranslator<F extends Frame<S>, S> {

    F getFrame(S subject);

    Set<OWLAxiom> getAxioms(F frame, Mode mode);
}
