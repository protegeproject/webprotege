package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.shared.frame.Frame;
import org.semanticweb.owlapi.model.OWLAxiom;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/01/2013
 */
public interface FrameTranslator<F extends Frame<S>, S> {

    @Nonnull
    F getFrame(@Nonnull S subject);

    @Nonnull
    Set<OWLAxiom> getAxioms(@Nonnull F frame, @Nonnull Mode mode);
}
