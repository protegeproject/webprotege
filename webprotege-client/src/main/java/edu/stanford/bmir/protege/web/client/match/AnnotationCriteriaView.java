package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;
import edu.stanford.bmir.protege.web.shared.match.AnnotationPresence;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Jun 2018
 */
public interface AnnotationCriteriaView extends IsWidget {

    void setSelectedProperty(@Nonnull OWLAnnotationPropertyData data);

    Optional<OWLAnnotationProperty> getSelectedProperty();

    @Nonnull
    AcceptsOneWidget getValueCriteriaViewContainer();

    void clearProperty();
}
