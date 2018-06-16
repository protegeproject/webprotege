package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.user.client.ui.IsWidget;
import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 15 Jun 2018
 */
public interface EntityTypeCriteriaView extends IsWidget {

    @Nonnull
    Set<EntityType<?>> getEntityTypes();

    void setEntityTypes(@Nonnull Set<EntityType<?>> entityType);
}
