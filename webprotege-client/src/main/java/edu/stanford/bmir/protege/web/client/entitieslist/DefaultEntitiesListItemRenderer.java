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
        StringBuilder title = new StringBuilder();
        entity.getShortForms().forEach((lang, sf) -> {
            title.append(lang.getLang()).append(": ").append(sf).append("\n");
        });
        sb.append("<span title='").append(title.toString()).append("'>");

        sb.append(entity.getBrowserText());
        sb.append("</span>");
    }
}
