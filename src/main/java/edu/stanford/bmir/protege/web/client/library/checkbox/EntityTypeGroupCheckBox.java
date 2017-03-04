package edu.stanford.bmir.protege.web.client.library.checkbox;

import com.google.common.base.Optional;
import com.google.gwt.user.client.ui.CheckBox;
import edu.stanford.bmir.protege.web.shared.entity.EntityTypeGroup;
import org.semanticweb.owlapi.model.EntityType;

import java.util.Iterator;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/07/2013
 */
public class EntityTypeGroupCheckBox extends CheckBox {

    private EntityTypeGroup entityTypeGroup;

    public Optional<EntityTypeGroup> getEntityTypeGroup() {
        return Optional.fromNullable(entityTypeGroup);
    }

    public void setEntityTypeGroup(EntityTypeGroup entityTypeGroup) {
        this.entityTypeGroup = entityTypeGroup;
        StringBuilder sb = new StringBuilder();
        for(Iterator<EntityType<?>> it = entityTypeGroup.getEntityTypes().iterator(); it.hasNext(); ) {
            EntityType<?> entityType = it.next();
            sb.append(entityType.getVocabulary().getNamespace().name().toLowerCase());
            sb.append(":");
            sb.append(entityType.getVocabulary().getShortForm());
            if(it.hasNext()) {
                sb.append(", ");
            }
        }
        if(sb.length() > 0) {
            sb.insert(0, "(");
            sb.append(")");
        }
        setHTML(entityTypeGroup.getDisplayName() + "    <span style=\"color: gray; font-size: 90%;\">" + sb.toString() + "</span>");
    }
}
