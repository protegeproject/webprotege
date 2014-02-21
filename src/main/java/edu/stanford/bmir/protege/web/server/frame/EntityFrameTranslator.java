package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.shared.frame.EntityFrame;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/01/2013
 */
public interface EntityFrameTranslator<F extends EntityFrame<E>, E extends OWLEntity> extends FrameTranslator<F, E> {

    /**
     * Gets the entity type that this translator translates.
     * @return The entity type.  Not {@code null}.
     */
    EntityType<E> getEntityType();

}
