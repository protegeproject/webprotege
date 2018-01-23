package edu.stanford.bmir.protege.web.client.entitieslist;

import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/09/2013
 */
public interface EntitiesListItemRenderer<E extends OWLEntityData> {

    void render(E entity, StringBuilder sb);
}
