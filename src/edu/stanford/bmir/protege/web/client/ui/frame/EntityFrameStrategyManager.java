package edu.stanford.bmir.protege.web.client.ui.frame;

import org.semanticweb.owlapi.model.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/01/2013
 */
public class EntityFrameStrategyManager {

    // <L extends LabelledFrame<F, S>, F extends EntityFrame<S>,S extends OWLEntity>

    public FramePresenterStrategy<?, ?, ?> get(OWLEntity entity) {
        return new ClassFramePresenterStrategy();
    }
}
