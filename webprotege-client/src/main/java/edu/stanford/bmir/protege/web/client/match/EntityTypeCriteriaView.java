package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.user.client.ui.IsWidget;
import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 15 Jun 2018
 */
public interface EntityTypeCriteriaView extends IsWidget {

    @Nonnull
    EntityType<?> getEntityType();

    void setEntityType(@Nonnull EntityType<?> entityType);
}
