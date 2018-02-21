package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.frame.EntityFrame;
import org.semanticweb.owlapi.model.EntityType;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/01/2013
 */
public interface EntityFrameTranslator<F extends EntityFrame<E>, E extends OWLEntityData> extends FrameTranslator<F, E> {

    /**
     * Gets the entity type that this translator translates.
     * @return The entity type.  Not {@code null}.
     */
    EntityType<?> getEntityType();

}
