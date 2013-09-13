package edu.stanford.bmir.protege.web.client.entitieslist;

import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/09/2013
 */
public class DefaultEntitiesListItemRenderer<E extends OWLEntityData> implements EntitiesListItemRenderer<E> {

    @Override
    public void render(E entity, StringBuilder sb) {
        sb.append(entity.getBrowserText());
    }
}
