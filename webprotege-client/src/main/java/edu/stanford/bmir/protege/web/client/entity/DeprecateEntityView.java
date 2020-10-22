package edu.stanford.bmir.protege.web.client.entity;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-10-21
 */
public interface DeprecateEntityView extends IsWidget, HasRequestFocus {

    @Nonnull
    AcceptsOneWidget getFormContainer();

    void setParentEntityType(@Nonnull PrimitiveType entityType);

    @Nonnull
    Optional<OWLEntity> getReplacementEntity();
}
