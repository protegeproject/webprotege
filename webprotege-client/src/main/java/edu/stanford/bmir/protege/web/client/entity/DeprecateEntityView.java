package edu.stanford.bmir.protege.web.client.entity;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-10-21
 */
public interface DeprecateEntityView extends IsWidget {

    @Nonnull
    AcceptsOneWidget getFormContainer();

    void setParentEntityType(@Nonnull PrimitiveType entityType);
}
